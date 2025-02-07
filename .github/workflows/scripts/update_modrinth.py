"""
A script to read README.md, replace unsupported elements, and save the result to modrinth.md.
"""
__author__ = 'pugur523'

import re
import requests
import sys
import os

def format_modrinth():
  with open("README.md", "r", encoding="utf-8") as f:
    content = f.read()

  content = re.sub(r"\[!NOTE\]", "**💡 NOTE:**", content)
  content = re.sub(r"\[!CAUTION\]", "**⚠️ CAUTION:**", content)

  with open("modrinth.md", "w", encoding="utf-8") as f:
    f.write(content)

  print("✅ Converted content has been saved to modrinth.md")
  return content

def update_modrinth_desc(id, description):
  token = os.environ["MODRINTH_API_TOKEN"]

  headers = {
    "Content-Type": "application/json",
    "Authorization": f"Bearer {token}"
  }

  data = {
    "body": description
  }

  url = f"https://api.modrinth.com/v2/project/{id}"
  response = requests.patch(url, headers=headers, json=data)
  
  if response.status_code == 204:
    print("✅ modrinth description updated successfully")
  else:
    print(f"Failed to update description. Status code: {response.status_code}")
    body = response.json()
    print(body["error"])
    print(body["description"])

def main():
  args = sys.argv
  formatted_content = format_modrinth()
  update_modrinth_desc(args[1], formatted_content)
  
if __name__ == "__main__":
  main()