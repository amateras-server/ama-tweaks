#!/usr/bin/env python3

import argparse
import json
import os
import sys

import requests

scripts_dir = os.path.abspath(os.path.dirname(__file__))
root_dir = os.path.dirname(scripts_dir)


def main():
    parser = argparse.ArgumentParser(
        description="Check and update Minecraft mod dependencies."
    )
    parser.add_argument(
        "--fix",
        action="store_true",
        help="Update gradle.properties with the latest versions",
    )
    args = parser.parse_args()

    check_updates(fix=args.fix)
    return 0


def get_latest_modrinth_version(project_id, game_version, loader="fabric"):
    base_url = f"https://api.modrinth.com/v2/project/{project_id}"

    try:
        filter_url = f"{base_url}/version"
        params = {
            "loaders": json.dumps([loader]),
            "game_versions": json.dumps([game_version]),
        }
        res = requests.get(filter_url, params=params)
        if res.status_code != 200 or not res.json():
            return None

        latest_release = res.json()[0]
        latest_id = latest_release["id"]
        latest_ver_number = latest_release["version_number"]

        all_versions_url = f"{base_url}/version"
        all_res = requests.get(all_versions_url)

        if all_res.status_code == 200:
            exact_matches = sum(
                1 for v in all_res.json() if v["version_number"] == latest_ver_number
            )

            if exact_matches > 1:
                return latest_id

        return latest_ver_number

    except Exception as e:
        print(f"Error fetching {project_id}: {e}")
        return None


def load_properties(filepath):
    props = {}
    if not os.path.exists(filepath):
        return None

    with open(filepath, "r", encoding="utf-8") as f:
        for line in f:
            line = line.strip()
            if not line or line.startswith("#"):
                continue
            if "=" in line:
                key, val = line.split("=", 1)
                props[key.strip()] = val.strip()
    return props


def update_properties_file(filepath, updates):
    if not os.path.exists(filepath):
        return

    lines = []
    with open(filepath, "r", encoding="utf-8") as f:
        lines = f.readlines()

    new_lines = []
    for line in lines:
        stripped = line.strip()
        if not stripped or stripped.startswith("#"):
            new_lines.append(line)
            continue

        if "=" in stripped:
            key, _ = stripped.split("=", 1)
            key = key.strip()
            if key in updates:
                new_lines.append(f"{key}={updates[key]}\n")
                continue

        new_lines.append(line)

    with open(filepath, "w", encoding="utf-8") as f:
        f.writelines(new_lines)


def check_updates(fix=False):
    with open(os.path.join(root_dir, "settings.json"), "r") as f:
        game_versions = json.load(f)["versions"]

    with open(os.path.join(root_dir, "dependency_mods.json"), "r") as f:
        dependencies = json.load(f)

    for g_ver in reversed(game_versions):
        prop_path = os.path.join(root_dir, "versions", g_ver, "gradle.properties")
        props = load_properties(prop_path)

        if props is None:
            continue

        print(f"\n--- Minecraft {g_ver} ---")

        updates_found = 0
        pending_updates = {}

        for slug, prop_key in dependencies.items():
            current_version = props.get(prop_key)

            if current_version is None:
                continue

            latest_version = get_latest_modrinth_version(slug, g_ver)

            if latest_version:
                if current_version != latest_version:
                    if fix:
                        print(
                            f"[FIXED] {slug:15} | {current_version} -> {latest_version}"
                        )
                        pending_updates[prop_key] = latest_version
                    else:
                        print(
                            f"[UPDATE] {slug:15} | Local: {current_version} -> Latest: {latest_version}"
                        )
                    updates_found += 1
                    pass
            else:
                print(f"[NOT FOUND] {slug:18} : No release for {g_ver} on Modrinth")
                pass

        if fix and pending_updates:
            update_properties_file(prop_path, pending_updates)
            print(f"[INFO] Updated {prop_path}")

        if updates_found == 0:
            print("[OK] all dependencies are latest")


if __name__ == "__main__":
    sys.exit(main())
