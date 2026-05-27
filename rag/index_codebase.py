"""Index the DroidPokedex Kotlin codebase into a local Chroma vector store."""

from __future__ import annotations

from pathlib import Path

from chunking import KotlinASTChunker, KotlinChunk
from langchain.schema import Document
from langchain_community.vectorstores import Chroma
from langchain_huggingface import HuggingFaceEmbeddings

CODEBASE_ROOT = Path(__file__).resolve().parent.parent
CHROMA_PERSIST_DIR = Path(__file__).resolve().parent / "chroma_db"
COLLECTION_NAME = "droidpokedex"

EXCLUDE_DIRS = {"build", ".gradle", ".idea", "test", "androidTest", "generated"}
INCLUDE_PATTERNS = ("**/*.kt", "**/*.kts")
EMBEDDING_MODEL = "BAAI/bge-small-en-v1.5"


def get_local_embeddings() -> HuggingFaceEmbeddings:
    return HuggingFaceEmbeddings(
        model_name=EMBEDDING_MODEL,
        model_kwargs={"device": "cpu"},
        encode_kwargs={"normalize_embeddings": True},
    )


def should_index_file(file_path: Path) -> bool:
    return not bool(set(file_path.parts) & EXCLUDE_DIRS)


def collect_kotlin_files() -> list[Path]:
    files: list[Path] = []
    for pattern in INCLUDE_PATTERNS:
        for file_path in CODEBASE_ROOT.glob(pattern):
            if should_index_file(file_path):
                files.append(file_path)
    return sorted(files)


def infer_module(path_parts: tuple[str, ...]) -> str:
    for part in path_parts:
        if part in ("home", "details", "network", "app"):
            return part
    return "root"


def infer_layer(file_path: str, path_parts: tuple[str, ...]) -> str | None:
    if "domain" in path_parts:
        return "domain"
    if "data" in path_parts:
        return "data"
    if "viewmodel" in file_path.lower():
        return "presentation"
    if "designsystem" in file_path.lower():
        return "ui"
    return None


def build_description(chunk: KotlinChunk) -> str:
    if chunk.parent_class:
        return f"{chunk.chunk_type}: {chunk.parent_class}.{chunk.name}"
    return f"{chunk.chunk_type}: {chunk.name}"


def chunks_to_documents(chunks: list[KotlinChunk]) -> list[Document]:
    documents: list[Document] = []
    for chunk in chunks:
        relative_path = Path(chunk.file_path).relative_to(CODEBASE_ROOT)
        path_parts = relative_path.parts
        module = infer_module(path_parts)
        layer = infer_layer(chunk.file_path, path_parts)

        documents.append(
            Document(
                page_content=chunk.content,
                metadata={
                    "source": str(relative_path),
                    "chunk_type": chunk.chunk_type,
                    "name": chunk.name,
                    "parent_class": chunk.parent_class or "",
                    "start_line": chunk.start_line,
                    "end_line": chunk.end_line,
                    "module": module,
                    "layer": layer or "",
                    "description": build_description(chunk),
                },
            )
        )
    return documents


def main() -> None:
    print("=" * 60)
    print("DroidPokedex Codebase Indexer (local, no API keys)")
    print("=" * 60)

    print("\nFinding Kotlin files...")
    files = collect_kotlin_files()
    print(f"  Found {len(files)} files")

    print("\nParsing with Tree-sitter AST...")
    chunker = KotlinASTChunker()
    all_chunks: list[KotlinChunk] = []

    for file_path in files:
        try:
            chunks = chunker.chunk_file(file_path)
            all_chunks.extend(chunks)
            relative = file_path.relative_to(CODEBASE_ROOT)
            print(f"  {relative} -> {len(chunks)} chunks")
        except Exception as exc:
            print(f"  Failed {file_path.name}: {exc}")

    print(f"\n  Total chunks: {len(all_chunks)}")

    type_counts: dict[str, int] = {}
    for chunk in all_chunks:
        type_counts[chunk.chunk_type] = type_counts.get(chunk.chunk_type, 0) + 1

    print("\nChunk types:")
    for chunk_type, count in sorted(type_counts.items()):
        print(f"  {chunk_type}: {count}")

    print("\nCreating embeddings and storing in Chroma...")
    documents = chunks_to_documents(all_chunks)
    embeddings = get_local_embeddings()

    Chroma.from_documents(
        documents=documents,
        embedding=embeddings,
        collection_name=COLLECTION_NAME,
        persist_directory=str(CHROMA_PERSIST_DIR),
    )

    print(f"\nIndexing complete.")
    print(f"  Vector store: {CHROMA_PERSIST_DIR}")
    print(f"  Total chunks indexed: {len(documents)}")


if __name__ == "__main__":
    main()
