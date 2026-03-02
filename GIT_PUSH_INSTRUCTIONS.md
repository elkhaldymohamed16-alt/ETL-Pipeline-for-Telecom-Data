# Quick Git Push Guide

## Step 1: Initialize Git (if not already done)

```bash
cd c:\Users\elkha\IdeaProjects\telecom-etl
git init
```

## Step 2: Set Your Git Configuration

```bash
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"
```

## Step 3: Add All Files to Staging

```bash
git add .
```

Verify what will be committed (should NOT include sensitive files):
```bash
git status
```

Expected to be excluded (verified by .gitignore):
- ✓ target/
- ✓ .idea/
- ✓ data/cdr_data.csv
- ✓ application-local.properties
- ✓ .env files

## Step 4: Create Initial Commit

```bash
git commit -m "Initial commit: ETL Pipeline for Telecom Data - Clean and secure version"
```

## Step 5: Add Remote Repository

Replace `YOUR_USERNAME` with your GitHub username:

```bash
git remote add origin https://github.com/YOUR_USERNAME/ETL-Pipeline-for-Telecom-Data.git
```

Or if using SSH:
```bash
git remote add origin git@github.com:YOUR_USERNAME/ETL-Pipeline-for-Telecom-Data.git
```

## Step 6: Push to GitHub

First time push (sets up branch tracking):
```bash
git branch -M main
git push -u origin main
```

Subsequent pushes:
```bash
git push
```

---

## Verification Commands

### Check Git Status
```bash
git status
```

### View Commits
```bash
git log --oneline
```

### View Remote Configuration
```bash
git remote -v
```

### View Staged Files
```bash
git diff --cached --name-only
```

---

## Troubleshooting

### Authentication Issues

**Using HTTPS:**
- GitHub requires Personal Access Token (not password)
- Create token: https://github.com/settings/tokens
- Use token when prompted for password

**Using SSH:**
- Generate SSH key: `ssh-keygen -t ed25519 -C "your.email@example.com"`
- Add to GitHub: https://github.com/settings/keys
- Test: `ssh -T git@github.com`

### Repository Already Exists on GitHub

If you created empty repo on GitHub:
```bash
git remote remove origin
git remote add origin https://github.com/YOUR_USERNAME/ETL-Pipeline-for-Telecom-Data.git
git branch -M main
git push -u origin main
```

### Force Push (if needed)

Only if absolutely necessary:
```bash
git push -u origin main --force
```

---

## After Successful Push ✅

1. Verify on GitHub: https://github.com/YOUR_USERNAME/ETL-Pipeline-for-Telecom-Data
2. Check `.gitignore` is listed
3. Verify no sensitive files are visible
4. Share repository link with team
5. Update README.md with any additional information

---

## Important: Set Up Environment Variables

Users cloning your repository must set these environment variables:

```bash
# Windows
set DB_URL=jdbc:postgresql://localhost:5432/telecom_db
set DB_USERNAME=postgres
set DB_PASSWORD=your_password

# Linux/Mac
export DB_URL=jdbc:postgresql://localhost:5432/telecom_db
export DB_USERNAME=postgres
export DB_PASSWORD=your_password
```

Or create `src/main/resources/application-local.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/telecom_db
spring.datasource.username=postgres
spring.datasource.password=your_password
```
