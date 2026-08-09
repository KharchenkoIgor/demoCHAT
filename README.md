# demoCHAT — Full-Stack Messaging Platform (Discord Clone)
### デモチャット — フルスタック・リアルタイム通信プラットフォーム

This is a real-time communication platform inspired by Discord, built with **Spring Boot 4 (Java 21)** and **Vanilla JavaScript**. 
It features secure authentication, hierarchical server management, and live messaging via WebSockets.

このプロジェクトは、Discordにインスパイアされたリアルタイム通信プラットフォームで、最新の **Spring Boot 4** と **Java 21**、そして **JavaScript** で構築されています。
セキュアな認証、階層的なサーバー管理、WebSocketによるライブメッセージング機能を備えています。

---

## 🛠 Tech Stack / 技術スタック

### Backend / バックエンド
- **Language:** Java 21 (LTS)
- **Framework:** Spring Boot 4.0.2
- **Security:** Spring Security (Session-based, BCrypt Hashing)
- **Database:** MySQL 8 (Spring Data JPA, Hibernate)
- **Real-time:** Spring WebSocket (STOMP)
- **Utilities:** Project Lombok

### Frontend / フロントエンド
- **Language:** JavaScript (ES6+)
- **Protocol:** STOMP.js & SockJS (Real-time Communication)
- **Styling:** HTML5 & CSS3 (Modern UI/UX)

---

## 🏛 Architecture Highlights / アーキテクチャの特徴

This section summarizes the design decisions that are worth explaining in a technical interview.
技術面接で説明する価値のある設計判断をまとめています。

- **Package by Feature, not by Layer** — Instead of grouping classes by technical role (`controllers/`, `services/`, `repositories/`), the codebase is grouped by business feature (`features/chat`, `features/channel`, `features/server`, `features/auth`). Each feature folder owns its own Controller, Service, Repository, DTO and Mapper. This makes the codebase easier to navigate as it grows, and keeps related changes localized to one folder.
  技術的な役割（`controllers/`、`services/`、`repositories/`）ではなく、機能単位（`features/chat`、`features/channel`、`features/server`、`features/auth`）でパッケージを分割しています。各フィーチャーフォルダが自分専用の Controller・Service・Repository・DTO・Mapper を持つため、プロジェクトが大きくなってもコードを追いやすく、関連する変更が1つのフォルダに閉じます。

- **Use-Case Pattern inside Services** — Business operations that are more than trivial CRUD (`CreateMessage`, `UpdateMessage`, `DeleteMessage`, `CreateServer`, `JoinServer`, `AcceptedJoinRequest`, etc.) are extracted into their own single-purpose classes under a `usecase/` subfolder, each exposing one `execute(...)` method. The `Service` class (e.g. `MessageService`) becomes a thin façade that simply delegates to the right use case. This keeps each class focused on exactly one responsibility (Single Responsibility Principle) and makes unit testing straightforward — each use case can be tested in isolation.
  単純なCRUDを超えるビジネス処理（`CreateMessage`、`UpdateMessage`、`DeleteMessage`、`CreateServer`、`JoinServer`、`AcceptedJoinRequest`など）は、`usecase/`サブフォルダの中に、それぞれ1つの`execute(...)`メソッドだけを持つ単一目的のクラスとして切り出しています。`MessageService`のような`Service`クラスは、適切なユースケースに処理を委譲するだけの薄いファサードになります。これにより各クラスが単一責任の原則を守り、ユニットテストも書きやすくなります（各ユースケースを独立してテストできる）。

- **Pub/Sub broadcast via `ServerEventNotifier`** — Rather than having every feature (`ChatEventPublisher`, `ChannelEventPublisher`, `ServerEventPublisher`) talk to `SimpMessagingTemplate` directly, they all publish through one shared `ServerEventNotifier`. It looks up every `Member` of the target server and pushes the payload to each user's private STOMP queue (`/user/queue/events`). This centralizes the "who should receive this event" logic in one place instead of duplicating it across features.
  各フィーチャー（`ChatEventPublisher`、`ChannelEventPublisher`、`ServerEventPublisher`）が直接`SimpMessagingTemplate`を呼ぶのではなく、共通の`ServerEventNotifier`を経由して配信しています。対象サーバーの全`Member`を取得し、各ユーザー専用のSTOMPキュー（`/user/queue/events`）にペイロードを送信します。「誰にこのイベントを届けるべきか」というロジックを1箇所に集約し、各フィーチャーでの重複を防いでいます。

- **DTO + Mapper separation** — Entities (`Message`, `Channel`, `Server`, `User`) are never returned directly from a controller. Each feature has a dedicated `*DTO` and `*Mapper` (e.g. `MessageDTO` / `MessageMapper`) that converts between the JPA entity and the API-facing shape. This prevents accidental leaking of internal fields (like password hashes) and avoids infinite-recursion issues from bidirectional JPA relationships when serialized to JSON.
  Entity（`Message`、`Channel`、`Server`、`User`）をコントローラーから直接返すことはしません。各フィーチャーが専用の`*DTO`と`*Mapper`（例：`MessageDTO` / `MessageMapper`）を持ち、JPAエンティティとAPI用の形の変換を担います。これにより、パスワードハッシュなど内部フィールドの意図しない漏洩や、双方向のJPAリレーションをJSONにシリアライズする際の無限再帰を防いでいます。

- **Centralized error handling (`AppException` + `ErrorCode` + `GlobalExceptionHandler`)** — Business errors are thrown as a single `AppException` type carrying an `ErrorCode` enum (which maps to an HTTP status) and a human-readable message. A `@RestControllerAdvice` (`GlobalExceptionHandler`) catches this one exception type application-wide and converts it into a consistent `ErrorResponse` JSON shape, so the frontend always parses errors the same way regardless of which endpoint failed.
  ビジネスエラーは、HTTPステータスに対応する`ErrorCode`列挙型と人間が読めるメッセージを持つ、単一の`AppException`型としてスローされます。`@RestControllerAdvice`（`GlobalExceptionHandler`）がアプリ全体でこの例外型を一括捕捉し、統一された`ErrorResponse`のJSON形式に変換するため、フロントエンドはどのエンドポイントが失敗しても同じ方法でエラーを解析できます。

---

## 🚀 Roadmap & Progress / ロードマップと進捗状況

### ✅ Level 0: Foundation (Completed) / レベル 0: 基盤 (完了済み)
- [x] **Spring Security Integration** / Spring Security の統合
- [x] **Password Hashing (BCrypt)** / BCrypt によるパスワードの暗号化
- [x] **Session-based Auth** / セッションベースの認証実装
- [x] **MySQL & JPA Mapping** / MySQL と JPA によるリレーショナルマッピング
- [x] **Transactional Logic (`@Transactional`)** / トランザクション制御の実装

### 🛠 Refactoring & Optimization / リファクタリングと最適化
- [x] Boilerplate reduction with **Lombok** / Lombok によるコードの簡略化
- [x] Frontend JS Modularization / フロントエンド JS のモジュール化とクリーンアップ
- [x] CSS Variable System / CSS 変数システムによるスタイルの統一
- [ ] Spring Validation Transition / Spring Validation への移行 (手動の if チェックを削除)
- [x] **Package by Feature** Architecture / フィーチャー駆動パッケージ構成への移行
- [x] WebSocket Publisher Optimization / WebSocket配信ロジックの統合 (ServerEventNotifier)
- [x] **Stale Data Bugfixes (Frontend)** / フロントエンドの古いデータ表示バグの修正 (Stale Closure解消)
- [ ] **Dynamic Topic Routing** / 動的トピックルーティングへの移行 (`/topic/channel.{id}`)
  
### 🟢 Level 1: Core MVP / レベル 1: コア機能 (MVP)
- **Servers & Members / サーバーとメンバー**
  - [x] Create/Edit/Delete Server (Owner only) / サーバー作成・編集・削除 (所有者のみ)
  - [x] Auto-add Creator as OWNER / 作成者の自動オーナー権限付与
  - [x] Server Visibility (Public vs Private) / サーバーの公開・非公開設定
  - [x] **Join Request System** / 非公開サーバーへの参加申請システム
- **Channels Management / チャンネル管理**
  - [x] Create Text Channels / テキストチャンネルの作成
  - [x] Edit & Delete Channels / チャンネルの編集・削除
  - [ ] Channel Categories / チャンネルのカテゴリー分け (グループ化)
  - [ ] Channel-specific Permissions / チャンネルごとの権限設定
- **Real-time Messaging / リアルタイムメッセージ**
  - [x] **WebSocket Messaging (STOMP)** / WebSocket メッセージング (STOMP)
  - [x] Message Persistence (DB) / メッセージのデータベース保存
  - [x] Live Edit/Delete Messages / メッセージの編集・削除 (リアルタイム)
  - [x] Load Channel History / チャンネル履歴の読み込み
  - [x] Real-time Notifications / リアルタイム通知機能 (バッジ / トースト)

### 🛠 Level 2: Auth & Validation / レベル 2: 認証とバリデーション
- [ ] **Password Reset via Email (SMTP)** / パスワード再設定 (SMTP/メール連携)
- [x] Implementation of **DTOs** / APIレイヤーへの DTO 実装
- [ ] Bean Validation (`@Valid`, `@NotBlank`) / Bean Validation による入力チェック
- [x] Global Exception Handling / グローバル例外ハンドリング (`@RestControllerAdvice`)
- [ ] User Profile Edit (Avatar/Nickname) / ユーザープロフィールの編集 (アバター/ニックネーム)

### 🟡 Level 3: Social & UX / レベル 3: ソーシャルとUX
- [ ] Friend Request System / フレンド申請システム
- [ ] Online/Offline Status Indicator / オンライン状態表示
- [ ] Unique Server Invite Codes / サーバー招待用コード
- [ ] Message Replies & Mentions (`@user`) / 返信とメンション機能
- [ ] Emoji Reactions / メッセージへのリアクション機能
- [ ] Rich Text Formatting (Markdown) / Markdown サポート

### 🔵 Level 4: Direct Messages (DM) / レベル 4: ダイレクトメッセージ (DM)
- [ ] 1-on-1 Private Chat Rooms / 1対1のプライベートチャットルーム
- [ ] Dedicated Private WS Topics / DM専用のプライベートWebSocketトピック
- [ ] "User is typing..." Feature / 「入力中...」機能

### 🟠 Level 5: Dynamic Permissions / レベル 5: 動的な権限管理
- [ ] DB-driven Custom Roles / データベース管理によるカスタムロール
- [ ] Granular Permission Flags / 詳細な権限フラグ設定
- [ ] Role Hierarchy & Colors / ロールの階層構造とカスタムカラー
- [ ] Kick/Ban System / ユーザーの追放・BAN機能
- [ ] **Audit Logs** / サーバー内の監査ログ (履歴)

### 🔴 Level 6: Media & Voice / レベル 6: メディアとボイスチャット
- [ ] Image Attachments in Chat / チャットへの画像添付機能
- [ ] Link Previews (OpenGraph) / リンクプレビュー機能
- [ ] **Voice Channels (WebRTC)** / ボイスチャンネル (WebRTC 統合)

### 💼 Level 7: Professional Engineering / レベル 7: エンジニアリング
- [ ] **Unit & Integration Testing (JUnit, Mockito)** / 単体・統合テストの実装
- [ ] **API Documentation (Swagger/OpenAPI)** / APIドキュメントの自動生成
- [ ] **CI/CD Pipeline (GitHub Actions)** / CI/CD パイプラインの構築
- [ ] **Logging System (SLF4J/Logback)** / 適切なログ管理システム
- [ ] **Message Pagination** / メッセージのパギネーション (Infinite Scroll)
- [ ] Cloud File Storage (S3/MinIO) / クラウドファイルストレージ (S3/MinIO)
- [ ] Database Versioning (Flyway) / データベースのバージョン管理
- [ ] Docker Environment / Docker 環境構築 (docker-compose)

### 🏴‍☠️ Level 8: Advanced Auth (JWT) / レベル 8: 高度な認証 (JWT)
- [ ] Access & Refresh Token Logic / アクセス・リフレッシュトークン実装
- [ ] **JWT** Security Filter / JWT セキュリティフィルター
- [ ] WebSocket Auth via JWT / WebSocket での JWT 認証

---

## ⚙️ Installation / インストール

### English
1. Clone the repository: `git clone https://github.com/KharchenkoIgor/demoCHAT.git`
2. Configure `src/main/resources/application.properties` (Database & SMTP).
3. Build and run: `./mvnw spring-boot:run`

### 日本語
1. リポジトリをクローン: `git clone https://github.com/KharchenkoIgor/demoCHAT.git`
2. `src/main/resources/application.properties` を設定 (DBとSMTP)。
3. 実行: `./mvnw spring-boot:run`

---

## 🌐 Deployment Guide / デプロイ手順

Two deployment paths are documented below: a manual deploy directly onto an AWS EC2 instance, and a containerized deploy using Docker. Both assume the app listens on port `8080` and needs a MySQL 8 database.

以下では2種類のデプロイ方法を説明します：EC2インスタンスへの手動デプロイと、Dockerを使ったコンテナ化デプロイです。どちらも、アプリはポート`8080`で待ち受け、MySQL 8のデータベースが必要という前提です。

### A. Manual Deployment on AWS EC2 / AWS EC2への手動デプロイ

**Step 1 — Launch the EC2 instance / EC2インスタンスを起動する**
- AMI: Ubuntu Server 24.04 LTS
- Instance type: `t2.micro` (Free Tier eligible) is enough for a demo
- Create/select a Key Pair (`.pem`) — needed for SSH access
- Security Group: open port `22` (SSH, restrict to your IP), `80`/`443` (if you put a reverse proxy in front) or `8080` (if exposing Spring Boot directly)

**Step 2 — Connect via SSH / SSH接続**
```bash
chmod 400 MyKeyPair.pem
ssh -i MyKeyPair.pem ubuntu@<EC2_PUBLIC_IP>
```

**Step 3 — Install Java 21 and MySQL on the server / サーバーにJava 21とMySQLをインストール**
```bash
sudo apt update
sudo apt install -y openjdk-21-jdk mysql-server
sudo systemctl enable mysql
sudo systemctl start mysql
```

**Step 4 — Create the database and a dedicated app user / データベースと専用ユーザーを作成**
```bash
sudo mysql
```
```sql
CREATE DATABASE demochat_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER '*********'@'localhost' IDENTIFIED BY '*********';
GRANT ALL PRIVILEGES ON demochat_db.* TO '*********'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

**Step 5 — Build the JAR locally and transfer it to the server / ローカルでビルドし、サーバーへ転送**
```bash
# On your local machine, inside the project folder
./mvnw clean package -DskipTests

# Copy the built JAR to the server
scp -i mykey.pem target/demoChat-*.jar ubuntu@<EC2_PUBLIC_IP>:/home/ubuntu/app.jar
```

**Step 6 — Provide production configuration via environment variables / 環境変数で本番設定を渡す**
Never hardcode DB credentials into `application.properties`. Export them as environment variables instead, and reference them with `${...}` placeholders in `application.properties` (e.g. `spring.datasource.password=${DB_PASSWORD}`).
DBの認証情報を`application.properties`に直接書かないこと。環境変数として設定し、`application.properties`側では`${...}`のプレースホルダーで参照します（例：`spring.datasource.password=${DB_PASSWORD}`）。
```bash
echo 'export DB_URL="jdbc:mysql://localhost:3306/demochat_db"' >> ~/.bashrc
echo 'export DB_USER="*********"' >> ~/.bashrc
echo 'export DB_PASSWORD="*********"' >> ~/.bashrc
source ~/.bashrc
```

**Step 7 — Run the app as a `systemd` service (survives reboot & SSH disconnect) / systemdサービスとして常駐させる**
```bash
sudo nano /etc/systemd/system/demochat.service
```
```ini
[Unit]
Description=demoCHAT Spring Boot Application
After=network.target mysql.service

[Service]
User=ubuntu
EnvironmentFile=/home/ubuntu/.env
ExecStart=/usr/bin/java -jar /home/ubuntu/app.jar
SuccessExitStatus=143
Restart=always

[Install]
WantedBy=multi-user.target
```
```bash
sudo systemctl daemon-reload
sudo systemctl enable demochat
sudo systemctl start demochat
sudo systemctl status demochat
journalctl -u demochat -f   # tail the logs / ログをリアルタイムで確認
```

### B. Containerized Deployment with Docker / Dockerを使ったコンテナ化デプロイ

**Step 1 — Add a `Dockerfile` to the project root / プロジェクト直下にDockerfileを追加**
```dockerfile
# --- Build stage ---
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw clean package -DskipTests

# --- Run stage ---
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/demoChat-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```
A multi-stage build is used so the final image only contains the JRE and the built JAR — not the whole Maven build toolchain — keeping the image small.
最終イメージにはJRE（実行環境）とビルド済みJARのみが含まれ、Mavenのビルドツール一式は含まれません（マルチステージビルド）。これによりイメージサイズを小さく保てます。

**Step 2 — Add `docker-compose.yml` to run the app + MySQL together / アプリとMySQLをまとめて起動するdocker-compose.ymlを追加**
```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/demochat_db
      - SPRING_DATASOURCE_USERNAME=*********
      - SPRING_DATASOURCE_PASSWORD=*********
    depends_on:
      - db
  db:
    image: mysql:8
    environment:
      - MYSQL_ROOT_PASSWORD=rootpass
      - MYSQL_DATABASE=demochat_db
      - MYSQL_USER=*********
      - MYSQL_PASSWORD=*********
    volumes:
      - db_data:/var/lib/mysql
volumes:
  db_data:
```
Note: inside the Docker network, the app must connect to the database using the **service name** `db` as the host — not `localhost`.
注意：Dockerネットワーク内では、アプリはDBへ接続する際、ホスト名として`localhost`ではなく**サービス名の`db`**を使う必要があります。

**Step 3 — Build and run locally / ローカルでビルド・起動**
```bash
docker-compose up -d --build   # build the image and start both containers in the background
docker-compose logs -f app      # tail the app container's logs
docker-compose down             # stop and remove both containers
```

**Step 4 — Deploy the same setup to an EC2 instance / 同じ構成をEC2にデプロイ**
```bash
# On the EC2 instance
sudo apt update
sudo apt install -y docker.io docker-compose
sudo usermod -aG docker ubuntu   # allows running docker without sudo (re-login required)

# Transfer the project (or just Dockerfile + docker-compose.yml + source) to the server
scp -i mykey.pem -r ./demoCHAT ubuntu@<EC2_PUBLIC_IP>:/home/ubuntu/

# On the server
cd /home/ubuntu/demoCHAT
docker-compose up -d --build
```
Open Security Group port `8080` (or put Nginx in front on `80`/`443`) so the outside world can reach the container.
外部からアクセスできるよう、Security Groupでポート`8080`（またはNginxを前段に立てる場合は`80`/`443`）を開放してください。

---

## 📁 Project Structure Overview / プロジェクト構成の概要

```
src/main/java/project/demoChat/
├── config/            # Spring Security & WebSocket configuration / Spring SecurityとWebSocketの設定
├── domain/             # JPA entities (User, Server, Channel, Message, Member...) / JPAエンティティ
├── exception/          # AppException, ErrorCode, GlobalExceptionHandler
├── common/             # Cross-feature shared logic (ServerEventNotifier) / 機能横断の共通ロジック
└── features/
    ├── auth/            # Registration, login, UserRepository/Service
    ├── channel/         # Channel CRUD + usecase/ + websocket/
    ├── chat/             # Message CRUD + usecase/ + websocket/
    └── server/           # Server & membership logic + usecase/ + websocket/
```