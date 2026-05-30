#!/usr/bin/env python3
"""Generate Maestro UI test YAML from a PR diff via OpenAI and post as a PR comment."""

from __future__ import annotations

import json
import os
import re
import sys
from pathlib import Path

import requests
from openai import OpenAI

DIFF_CHAR_LIMIT = 12_000
GITHUB_API = "https://api.github.com"
MODEL = "gpt-4o"
COMMENT_HEADER = "🤖 Auto-Generated Maestro UI Test for this PR"
SYSTEM_PROMPT = "You are an Android Quality Engineer."
USER_PROMPT_TEMPLATE = (
    "Analyze this Git Diff. If any new UI elements (like buttons, text fields, or screens) "
    "were added in this PR, generate a valid Maestro UI test (YAML) to interact with them. "
    "Only output the YAML.\n\n"
    "Use appId: com.rodriguesalex.droidpokedex unless the diff indicates otherwise.\n\n"
    "```diff\n{diff}\n```"
)


def main() -> int:
    openai_key = os.environ.get("OPENAI_API_KEY", "").strip()
    github_token = os.environ.get("GITHUB_TOKEN", "").strip()

    if not openai_key or not github_token:
        print("API keys not configured. Skipping Maestro UI test generation.")
        return 0

    event_path = os.environ.get("GITHUB_EVENT_PATH")
    if not event_path or not Path(event_path).is_file():
        print("GITHUB_EVENT_PATH not set or missing. Skipping Maestro UI test generation.")
        return 0

    with open(event_path, encoding="utf-8") as f:
        event = json.load(f)

    pull_request = event.get("pull_request")
    if not pull_request:
        print("Not a pull_request event. Skipping Maestro UI test generation.")
        return 0

    pr_number = pull_request["number"]
    repo = event.get("repository", {})
    owner = repo.get("owner", {}).get("login") or event.get("organization", {}).get("login")
    repo_name = repo.get("name")

    if not owner or not repo_name:
        print("Could not determine repository from event payload. Skipping Maestro UI test generation.")
        return 0

    diff = fetch_pr_diff(owner, repo_name, pr_number, github_token)
    if not diff.strip():
        print("PR diff is empty. Skipping Maestro UI test generation.")
        return 0

    yaml_content = generate_maestro_yaml(diff, openai_key)
    if not yaml_content.strip():
        print("Model returned no YAML. Skipping PR comment.")
        return 0

    comment_body = format_pr_comment(yaml_content)
    post_pr_comment(owner, repo_name, pr_number, github_token, comment_body)
    print("Maestro UI test comment posted successfully.")
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


def generate_maestro_yaml(diff: str, openai_key: str) -> str:
    client = OpenAI(api_key=openai_key)
    completion = client.chat.completions.create(
        model=MODEL,
        messages=[
            {"role": "system", "content": SYSTEM_PROMPT},
            {"role": "user", "content": USER_PROMPT_TEMPLATE.format(diff=diff)},
        ],
        max_tokens=2000,
    )
    raw = (completion.choices[0].message.content or "").strip()
    return strip_yaml_fences(raw)


def strip_yaml_fences(text: str) -> str:
    """Remove markdown code fences if the model wrapped YAML in ``` blocks."""
    match = re.search(r"```(?:yaml|yml)?\s*\n(.*?)```", text, re.DOTALL | re.IGNORECASE)
    if match:
        return match.group(1).strip()
    return text.strip()


def format_pr_comment(yaml_content: str) -> str:
    return f"## {COMMENT_HEADER}\n\n```yaml\n{yaml_content}\n```"


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
        print(f"Maestro UI test generation failed: {e}", file=sys.stderr)
        sys.exit(1)
