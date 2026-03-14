#!/usr/bin/env python3

import json
import os
import sys

import requests

scripts_dir = os.path.abspath(os.path.dirname(__file__))
root_dir = os.path.join(scripts_dir, "..")


def main():
    check_updates()
    return 0


def get_latest_modrinth_version(project_id, game_version, loader="fabric"):
    url = f"https://api.modrinth.com/v2/project/{project_id}/version"
    params = {
        "loaders": json.dumps([loader]),
        "game_versions": json.dumps([game_version]),
    }

    try:
        response = requests.get(url, params=params)
        if response.status_code == 200:
            data = response.json()
            if data:
                return data[0]["version_number"]
        return None
    except Exception as e:
        print(f"Error fetching {project_id}: {e}")
        return None

def load_properties(filepath):
    props = {}
    if not os.path.exists(filepath):
        return None

    with open(filepath, 'r', encoding='utf-8') as f:
        for line in f:
            line = line.strip()
            if not line or line.startswith('#'):
                continue
            if '=' in line:
                key, val = line.split('=', 1)
                props[key.strip()] = val.strip()
    return props


def check_updates():
    with open(os.path.join(root_dir, "settings.json"), "r") as f:
        game_versions = json.load(f)["versions"]

    with open(os.path.join(root_dir, "dependency_mods.json"), "r") as f:
        dependencies = json.load(f)

    for g_ver in game_versions:
        prop_path = os.path.join(root_dir, "versions", g_ver, "gradle.properties")
        props = load_properties(prop_path)

        if props is None:
            continue

        print(f"\n--- Minecraft {g_ver} ---")

        updates_found = 0
        for slug, prop_key in dependencies.items():
            current_version = props.get(prop_key)

            if current_version is None:
                continue

            latest_version = get_latest_modrinth_version(slug, g_ver)

            if latest_version:
                if current_version != latest_version:
                    print(f"[UPDATE] {slug:15} | Local: {current_version} -> Latest: {latest_version}")
                    updates_found += 1
                    pass
            else:
                print(f"[NOT FOUND] {slug:18} : No release for {g_ver} on Modrinth")
                pass

        if updates_found == 0:
            print("[OK] all dependencies are latest")


if __name__ == "__main__":
    sys.exit(main())
