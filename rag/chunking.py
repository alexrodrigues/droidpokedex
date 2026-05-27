"""AST-based Kotlin code chunking using Tree-sitter."""

from __future__ import annotations

from dataclasses import dataclass
from pathlib import Path
from typing import Optional

import tree_sitter_kotlin as ts_kotlin
from tree_sitter import Language, Parser


@dataclass
class KotlinChunk:
    """A semantic chunk extracted from Kotlin source code."""

    content: str
    file_path: str
    chunk_type: str
    name: str
    start_line: int
    end_line: int
    parent_class: Optional[str] = None


DECLARATION_TYPES = {
    "class_declaration": "class",
    "object_declaration": "object",
    "interface_declaration": "interface",
    "function_declaration": "function",
}


class KotlinASTChunker:
    """Extract semantic chunks from Kotlin files using Tree-sitter AST."""

    def __init__(self) -> None:
        self._language = Language(ts_kotlin.language())
        self._parser = Parser(self._language)

    @staticmethod
    def _get_node_text(node, source_bytes: bytes) -> str:
        return source_bytes[node.start_byte : node.end_byte].decode("utf-8")

    @staticmethod
    def _get_line_numbers(node) -> tuple[int, int]:
        return (node.start_point[0] + 1, node.end_point[0] + 1)

    def _find_identifier(self, node) -> Optional[str]:
        for child in node.children:
            if child.type in ("simple_identifier", "type_identifier"):
                return child.text.decode("utf-8")
        return None

    def _extract_header(self, root, source_bytes: bytes) -> str:
        header_lines: list[str] = []
        for child in root.children:
            if child.type in ("package_header", "import_list", "import_header"):
                header_lines.append(self._get_node_text(child, source_bytes))
        return "\n".join(header_lines)

    def _is_data_class(self, node, source_bytes: bytes) -> bool:
        for child in node.children:
            if child.type == "modifiers":
                modifier_text = self._get_node_text(child, source_bytes)
                if "data" in modifier_text.split():
                    return True
        return False

    def _find_class_body(self, node):
        for child in node.children:
            if child.type == "class_body":
                return child
        return None

    def _extract_declarations(
        self,
        node,
        source_bytes: bytes,
        file_path: str,
        header: str,
        chunks: list[KotlinChunk],
        parent_class: Optional[str],
    ) -> None:
        for child in node.children:
            if child.type in DECLARATION_TYPES:
                chunk_type = DECLARATION_TYPES[child.type]
                name = self._find_identifier(child) or "anonymous"
                content = self._get_node_text(child, source_bytes)
                start_line, end_line = self._get_line_numbers(child)

                if child.type == "class_declaration" and self._is_data_class(
                    child, source_bytes
                ):
                    chunk_type = "data_class"

                full_content = f"{header}\n\n{content}" if header else content
                chunks.append(
                    KotlinChunk(
                        content=full_content,
                        file_path=file_path,
                        chunk_type=chunk_type,
                        name=name,
                        start_line=start_line,
                        end_line=end_line,
                        parent_class=parent_class,
                    )
                )

                if child.type in ("class_declaration", "object_declaration"):
                    class_body = self._find_class_body(child)
                    if class_body is not None:
                        self._extract_declarations(
                            class_body,
                            source_bytes,
                            file_path,
                            header,
                            chunks,
                            parent_class=name,
                        )
            else:
                self._extract_declarations(
                    child,
                    source_bytes,
                    file_path,
                    header,
                    chunks,
                    parent_class,
                )

    def chunk_file(self, file_path: Path) -> list[KotlinChunk]:
        """Parse a Kotlin file and return semantic chunks."""
        content = file_path.read_text(encoding="utf-8")
        source_bytes = content.encode("utf-8")
        tree = self._parser.parse(source_bytes)
        root = tree.root_node
        header = self._extract_header(root, source_bytes)

        chunks: list[KotlinChunk] = []
        self._extract_declarations(
            root,
            source_bytes,
            str(file_path.resolve()),
            header,
            chunks,
            parent_class=None,
        )
        return chunks
