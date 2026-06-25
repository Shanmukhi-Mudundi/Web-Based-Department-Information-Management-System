# images

This directory is for repository screenshots, GIFs, and other demo images referenced from the project README.

Place image files here (recommended names):
- screenshot-home.png
- screenshot-timetable.png
- flow-create-announcement.gif

How to add images

Option A — from your local machine (git):

1. Copy images into this folder from the repository root:
   ```bash
   mkdir -p docs/images
   cp /path/to/screenshot.png docs/images/screenshot-home.png
   git add docs/images/screenshot-home.png
   git commit -m "docs(images): add screenshot-home.png"
   git push origin your-branch
   ```

Option B — GitHub web UI:

1. Open this repository on GitHub, navigate to `docs/images/`, click "Add file" → "Upload files", drag & drop your images and commit to a branch.

Referencing images in README.md

Use relative paths so GitHub can render them in the README. Example:

```markdown
![Home page screenshot](docs/images/screenshot-home.png)

[![Home page screenshot](docs/images/screenshot-home.png)](docs/images/screenshot-home.png)

<img src="docs/images/screenshot-home.png" width="700" alt="Home page screenshot">
```

Notes

- Keep images small (<500 KB) if possible. Use PNG for screenshots and GIF for short flows.
- If you'd like, attach the screenshots here and I can add them and update the README for you.