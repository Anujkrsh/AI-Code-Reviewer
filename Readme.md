# 🤖 AI Code Reviewer

An open-source AI-powered GitHub Pull Request review bot built with **Java 21** and **Spring Boot**.  
It listens for GitHub PR webhooks, fetches the diff, sends it to **Google Gemini 2.0 Flash** for analysis, and posts a structured review comment directly on the PR — automatically, on every open or update.

---

## 📸 Demo

> The bot automatically comments on every PR with a structured review:

```
## 🤖 AI Code Review Results
### Summary: The changes introduce a new user registration endpoint...
### Verdict: REQUEST_CHANGES

### Comments:
#### 1. CRITICAL
- File: src/main/java/com/example/UserService.java
- Issue: Password stored in plain text
- Suggestion: Use BCryptPasswordEncoder before persisting

#### 2. WARNING
- File: src/main/java/com/example/UserController.java
- Issue: No input validation on request body
- Suggestion: Add @Valid and @NotBlank annotations
```

---

## ⚙️ How It Works

```
GitHub PR opened/updated
        │
        ▼
POST /api/v1/pr-review/handler
        │
        ▼ (HMAC signature verified)
        │
        ▼ (200 OK returned immediately)
        │
        ▼ @Async pipeline kicks off
        │
        ├── Fetch PR diff from GitHub
        ├── Build Gemini prompt
        ├── Call Gemini 2.0 Flash API
        ├── Parse structured JSON response
        │
        ▼
Post review comment on GitHub PR
```

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Framework | Spring Boot 3.5 + Java 21 |
| AI Model | Google Gemini 2.0 Flash (free tier) |
| Async Processing | `@Async` + `ThreadPoolTaskExecutor` |
| HTTP Client | Spring WebFlux `WebClient` |
| Build Tool | Gradle (Kotlin DSL) |
| Database | H2 in-memory |

---

## 🚀 Getting Started

### Prerequisites

- Java 21+
- Gradle
- A [GitHub Personal Access Token](https://github.com/settings/tokens) with `repo` scope
- A [Gemini API Key](https://aistudio.google.com) (free)
- [ngrok](https://ngrok.com) for local webhook testing

---

### 1. Clone the repo

```bash
git clone https://github.com/Anujkrsh/AI-Code-Reviewer.git
cd AI-Code-Reviewer
```

---

### 2. Configure application properties

Create `src/main/resources/application.yml`:

```yaml
github:
  token: your-github-personal-access-token
  webhook:
    secret: your-webhook-secret

gemini:
  key: your-gemini-api-key
  model: gemini-2.0-flash
```

> ⚠️ Never commit this file. It is already in `.gitignore`.

---

### 3. Run the app

```bash
./gradlew bootRun
```

The server starts on `http://localhost:8080`.

---

### 4. Expose locally with ngrok

```bash
ngrok http 8080
```

Copy the forwarding URL, e.g. `https://abc123.ngrok.io`.

---

### 5. Register the webhook on GitHub

1. Go to your repository → **Settings** → **Webhooks** → **Add webhook**
2. Set **Payload URL** to:
   ```
   https://abc123.ngrok.io/api/v1/pr-review/handler
   ```
3. Set **Content type** to `application/json`
4. Set **Secret** to the same value as `github.webhook.secret` in your config
5. Under **Which events**, select **Pull requests**
6. Click **Add webhook**

---

### 6. Test it

Open or update a Pull Request in your repository.  
The bot will automatically post a review comment within a few seconds.

---

### Local Testing (skip signature validation)

To test locally without a real GitHub webhook, send a request with the `source: local` header:

```bash
curl -X POST http://localhost:8080/api/v1/pr-review/handler \
  -H "Content-Type: application/json" \
  -H "X-Hub-Signature-256: sha256=dummy" \
  -H "source: local" \
  -d @sample-payload.json
```

---

## 📁 Project Structure

```
src/main/java/com/olivedevs/aicodereviewer/
├── controller/
│   └── WebController.java           # Webhook endpoint
├── security/
│   ├── GithubWebhookSignatureValidator.java  # HMAC-SHA256 verification
│   └── GithubWebhookEventValidator.java      # Payload parsing
├── service/
│   ├── DiffProcessor.java           # Fetches PR diff from GitHub
│   ├── GeminiCall.java              # Calls Gemini API
│   ├── GithubStringFormatter.java   # Formats review as markdown
│   ├── FinalGithubCall.java         # Posts comment to GitHub PR
│   └── Impl/
│       └── ReviewPipelineImpl.java  # Orchestrates the full pipeline (@Async)
├── dtos/
│   ├── ReviewResult.java            # Gemini response model
│   ├── ReviewComment.java           # Individual comment model
│   ├── Severity.java                # CRITICAL / WARNING / SUGGESTION
│   ├── PromptTemplate.java          # Gemini prompt
│   └── github/                      # GitHub webhook payload DTOs
├── config/
│   ├── AsyncConfig.java             # Thread pool configuration
│   └── WebClientConfig.java         # WebClient beans (GitHub + Gemini)
└── exception/                       # Custom exceptions + global handler
```

---

## 🔐 Security

- All incoming webhooks are verified using **HMAC-SHA256** signature validation (`X-Hub-Signature-256`)
- API keys and tokens are never hardcoded — loaded from `application.yml` via Spring `@Value`
- Signature validation can be bypassed locally using `source: local` header for development

---

## 🗺️ Roadmap

- [x] GitHub webhook receiver with HMAC validation
- [x] Async PR review pipeline
- [x] Gemini 2.0 Flash integration
- [x] Structured review comment on PR
- [ ] Inline diff comments (line-level)
- [ ] Multi-file chunking for large PRs
- [ ] Docker support
- [ ] PostgreSQL persistence
- [ ] Feedback loop (dev ratings → prompt improvement)
- [ ] Kafka-based event processing for scale

---

## 🤝 Contributing

Contributions are welcome! Please open an issue first to discuss what you'd like to change.

1. Fork the repo
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Commit your changes: `git commit -m 'Add your feature'`
4. Push to the branch: `git push origin feature/your-feature`
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License.

---

<p align="center">Built with ☕ Java + 🤖 Gemini by <a href="https://github.com/Anujkrsh">Anujkrsh</a></p>