"""Query the indexed DroidPokedex codebase."""

from __future__ import annotations

from pathlib import Path
from typing import Any

from langchain_community.vectorstores import Chroma
from langchain_huggingface import HuggingFaceEmbeddings

CHROMA_PERSIST_DIR = Path(__file__).resolve().parent / "chroma_db"
COLLECTION_NAME = "droidpokedex"
EMBEDDING_MODEL = "BAAI/bge-small-en-v1.5"


def get_vectorstore() -> Chroma:
    embeddings = HuggingFaceEmbeddings(
        model_name=EMBEDDING_MODEL,
        model_kwargs={"device": "cpu"},
        encode_kwargs={"normalize_embeddings": True},
    )
    return Chroma(
        collection_name=COLLECTION_NAME,
        persist_directory=str(CHROMA_PERSIST_DIR),
        embedding_function=embeddings,
    )


def search(
    query: str,
    k: int = 5,
    filter_dict: dict[str, Any] | None = None,
) -> list[dict[str, Any]]:
    """Search for relevant code chunks."""
    vectorstore = get_vectorstore()
    chroma_filter = _normalize_filter(filter_dict)
    results = vectorstore.similarity_search_with_score(
        query,
        k=k,
        filter=chroma_filter,
    )

    formatted: list[dict[str, Any]] = []
    for doc, score in results:
        similarity = max(0.0, 1.0 - score)
        formatted.append(
            {
                "content": doc.page_content,
                "source": doc.metadata.get("source"),
                "module": doc.metadata.get("module"),
                "layer": doc.metadata.get("layer"),
                "description": doc.metadata.get("description"),
                "chunk_type": doc.metadata.get("chunk_type"),
                "start_line": doc.metadata.get("start_line"),
                "end_line": doc.metadata.get("end_line"),
                "relevance_score": similarity,
            }
        )
    return formatted


def generate_context(query: str, k: int = 4) -> str:
    """Generate prompt-ready context from the most relevant chunks."""
    vectorstore = get_vectorstore()
    results = vectorstore.similarity_search(query, k=k)

    context_parts: list[str] = []
    for doc in results:
        meta = doc.metadata
        context_parts.append(
            f"// File: {meta.get('source', 'unknown')} "
            f"(lines {meta.get('start_line')}-{meta.get('end_line')})\n"
            f"// {meta.get('description', '')}\n"
            f"{doc.page_content}"
        )
    return "\n\n---\n\n".join(context_parts)


def _normalize_filter(filter_dict: dict[str, Any] | None) -> dict[str, Any] | None:
    if not filter_dict:
        return None
    return {key: value for key, value in filter_dict.items() if value}


def _print_results(query: str, results: list[dict[str, Any]], filter_dict: dict | None) -> None:
    print(f"\nQuery: '{query}'")
    if filter_dict:
        print(f"Filter: {filter_dict}")
    print("=" * 60)

    for index, result in enumerate(results, start=1):
        print(f"\nResult {index} (similarity: {result['relevance_score']:.3f})")
        print(f"  {result.get('description', 'N/A')}")
        print(f"  File: {result.get('source')}")
        print(f"  Lines: {result.get('start_line')}-{result.get('end_line')}")
        print(f"  Module: {result.get('module')} | Layer: {result.get('layer')}")
        preview = result["content"][:300].replace("\n", "\n  ")
        print(f"  Preview:\n  {preview}...")


if __name__ == "__main__":
    print("=" * 60)
    print("DroidPokedex Code Search")
    print("=" * 60)

    _print_results(
        "How does the home screen load Pokemon?",
        search("How does the home screen load Pokemon?"),
        None,
    )

    _print_results(
        "state management",
        search("state management", filter_dict={"chunk_type": "class"}),
        {"chunk_type": "class"},
    )

    _print_results(
        "Pokemon data model",
        search("Pokemon data model", filter_dict={"layer": "domain"}),
        {"layer": "domain"},
    )

    print("\n" + "=" * 60)
    print("Generated context for prompt:")
    print("=" * 60)
    context = generate_context("How does pagination work?", k=3)
    print(context[:1500] + ("..." if len(context) > 1500 else ""))
