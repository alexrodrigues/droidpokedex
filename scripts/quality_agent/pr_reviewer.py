#!/usr/bin/env python3
"""AI-powered PR reviewer: fetches diff, summarizes via OpenAI, posts a PR comment."""

from __future__ import annotations

import json
import os
import sys
from pathlib import Path

import requests
from openai import OpenAI

DIFF_CHAR_LIMIT = 12_000
GITHUB_API = "https://api.github.com"
MODEL = "gpt-4o-mini"


def main() -> int:
    openai_key = os.environ.get("OPENAI_API_KEY", "").strip()
    github_token = os.environ.get("GITHUB_TOKEN", "").strip()

    if not openai_key or not github_token:
        print("API keys not configured. Skipping AI review.")
        return 0

    event_path = os.environ.get("GITHUB_EVENT_PATH")
    if not event_path or not Path(event_path).is_file():
        print("GITHUB_EVENT_PATH not set or missing. Skipping AI review.")
        return 0

    with open(event_path, encoding="utf-8") as f:
        event = json.load(f)

    pull_request = event.get("pull_request")
    if not pull_request:
        print("Not a pull_request event. Skipping AI review.")
        return 0

    pr_number = pull_request["number"]
    repo = event.get("repository", {})
    owner = repo.get("owner", {}).get("login") or event.get("organization", {}).get("login")
    repo_name = repo.get("name")

    if not owner or not repo_name:
        print("Could not determine repository from event payload. Skipping AI review.")
        return 0

    diff = fetch_pr_diff(owner, repo_name, pr_number, github_token)
    if not diff.strip():
        print("PR diff is empty. Skipping AI review.")
        return 0

    review_body = generate_review(diff, openai_key)
    post_pr_comment(owner, repo_name, pr_number, github_token, review_body)
    print("AI review posted successfully.")
    return 0


def fetch_pr_diff(owner: str, repo: str, pr_number: int, token: str) -> str:
    url = f"{GITHUB_API}/repos/{owner}/{repo}/pulls/{pr_number}"
    headers = {
        "Authorization": f"Bearer {token}",
        "Accept": "application/vnd.github.v3.diff",
        "X-GitHub-Api-Version": "2022-11-28",
    }
    response = requests.get(url, headers=headers, timeout=120)
    response.raise_for_status()
    diff = response.text
    if len(diff) > DIFF_CHAR_LIMIT:
        diff = diff[:DIFF_CHAR_LIMIT] + "\n\n... (diff truncated for model context)"
    return diff


def generate_review(diff: str, openai_key: str) -> str:
    client = OpenAI(api_key=openai_key)
    prompt = (
        "You are reviewing a pull request for an Android Kotlin project (Jetpack Compose, "
        "Hilt, multi-module Clean Architecture).\n\n"
        "Analyze the following git diff and provide:\n"
        "1. **Architectural impact** — how the changes affect module boundaries, layers, and design.\n"
        "2. **Potential risks** — bugs, regressions, missing tests, security, or performance concerns.\n\n"
        "Be concise and actionable. Use markdown.\n\n"
        f"```diff\n{diff}\n```"
    )
    completion = client.chat.completions.create(
        model=MODEL,
        messages=[
            {
                "role": "system",
                "content": "You are a senior Android engineer performing code review.",
            },
            {"role": "user", "content": prompt},
        ],
        max_tokens=1500,
    )
    content = completion.choices[0].message.content or ""
    return "## AI PR Review\n\n" + content.strip()


def post_pr_comment(owner: str, repo: str, pr_number: int, token: str, body: str) -> None:
    url = f"{GITHUB_API}/repos/{owner}/{repo}/issues/{pr_number}/comments"
    headers = {
        "Authorization": f"Bearer {token}",
        "Accept": "application/vnd.github+json",
        "X-GitHub-Api-Version": "2022-11-28",
    }
    response = requests.post(url, headers=headers, json={"body": body}, timeout=60)
    response.raise_for_status()


if __name__ == "__main__":
    try:
        sys.exit(main())
    except requests.HTTPError as e:
        print(f"GitHub API error: {e}", file=sys.stderr)
        if e.response is not None:
            print(e.response.text, file=sys.stderr)
        sys.exit(1)
    except Exception as e:
        print(f"AI review failed: {e}", file=sys.stderr)
        sys.exit(1)
