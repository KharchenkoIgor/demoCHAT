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

## 🚀 Roadmap & Progress / ロードマップと進捗状況

### ✅ Level 0: Foundation (Completed) / レベル 0: 基盤 (完了済み)
- [x] **Spring Security Integration** / Spring Security の統合
- [x] **Password Hashing (BCrypt)** / BCrypt によるパスワードの暗号化
- [x] **Session-based Auth** / セッションベースの認証実装
- [x] **MySQL & JPA Mapping** / MySQL と JPA によるリレーショナルマッピング
- [x] **Transactional Logic (`@Transactional`)** / トランザクション制御の実装

### 🛠 Refactoring & Optimization / リファクタリングと最適化
- [x] **Boilerplate reduction with Lombok** / Lombok によるコードの簡略化
- [x] **Frontend JS Modularization / フロントエンド JS のモジュール化とクリーンアップ
- [x] **CSS Variable System / CSS 変数システムによるスタイルの統一
- [ ] **Spring Validation Transition / Spring Validation への移行 (手動の if チェックを削除)
- [ ] **Global Exception Handling / グローバル例外ハンドリング (@RestControllerAdvice)
- [ ] **[Refactor] Dynamic Topic Routing** / 動的トピックルーティングへの移行 (`/topic/channel.{id}`)
  
### 🟢 Level 1: Core MVP / レベル 1: コア機能 (MVP)
- **Servers & Members / サーバーとメンバー**
  - [x] Create/Edit/Delete Server (Owner only) / サーバー作成・編集・削除 (所有者のみ)
  - [x] Auto-add Creator as **OWNER** / 作成者の自動オーナー権限付与
  - [x] **Server Visibility (Public vs Private)** / サーバーの公開・非公開設定
  - [x] Join Request System (for Private) / 非公開サーバーへの参加申請システム
- **Channels Management / チャンネル管理**
  - [x] **Create Text Channels** / テキストチャンネルの作成
  - [x] **Edit & Delete Channels** / チャンネルの編集・削除
  - [ ] **Channel Categories** (Grouping) / チャンネルのカテゴリー分け (グループ化)
  - [ ] Channel-specific Permissions / チャンネルごとの権限設定
- **Real-time Messaging / リアルタイムメッセージ**
  - [x] WebSocket Messaging (STOMP) / WebSocket メッセージング (STOMP)
  - [x] Message Persistence (DB) / メッセージのデータベース保存
  - [x] Live Edit/Delete Messages / メッセージの編集・削除 (リアルタイム)
  - [x] Load Channel History / チャンネル履歴の読み込み

### 🛠 Level 2: Auth & Validation / レベル 2: 認証とバリデーション
- [ ] **Password Reset via Email (SMTP)** / パスワード再設定 (SMTP/メール連携)
- [x] Implementation of **DTOs** / APIレイヤーへの DTO 実装
- [ ] Bean Validation (`@Valid`, `@NotBlank`) / Bean Validation による入力チェック
- [ ] Global Exception Handling / グローバル例外ハンドリング (`@RestControllerAdvice`)

### 🟡 Level 3: Social & UX / レベル 3: ソーシャルとUX
- [ ] Friend Request System / フレンド申請システム
- [ ] Online/Offline Status Indicator / オンライン状態表示
- [ ] Block/Blacklist System / ブロック・ブラックリスト機能
- [ ] Unique Server Invite Codes / サーバー招待用コード

### 🔵 Level 4: Direct Messages (DM) / レベル 4: ダイレクトメッセージ (DM)
- [ ] 1-on-1 Private Chat Rooms / 1対1のプライベートチャットルーム
- [ ] Dedicated Private WS Topics / DM専用のプライベートWebSocketトピック
- [ ] "User is typing..." Feature / 「入力中...」機能

### 🟠 Level 5: Dynamic Permissions / レベル 5: 動的な権限管理
- [ ] DB-driven Custom Roles / データベース管理によるカスタムロール
- [ ] Granular Permission Flags / 詳細な権限フラグ設定
- [ ] Role Hierarchy & Colors / ロールの階層構造とカスタムカラー

### 🔴 Level 6: Media & Voice / レベル 6: メディアとボイスチャット
- [ ] Image Attachments in Chat / チャットへの画像添付機能
- [ ] User & Server Profile Pictures / プロフィール画像設定
- [ ] **Voice Channels (WebRTC)** / ボイスチャンネル (WebRTC 統合)

### 💼 Level 7: Professional Engineering / レベル 7: エンジニアリング
- [ ] **Message Pagination** / メッセージのパギネーション (Infinite Scroll)
- [ ] Cloud File Storage (S3/MinIO) / クラウドファイルストレージ (S3/MinIO)
- [ ] Database Versioning (Flyway) / データベースのバージョン管理
- [ ] Docker Environment / Docker 環境構築 (docker-compose)

### 🏴‍☠️ Level 8: Advanced Auth (JWT) / レベル 8: 高度な認証 (JWT)
- [ ] Access & Refresh Token Logic / アクセス・リフレッシュトークン実装
- [ ] JWT Security Filter / JWT セキュリティフィルター
- [ ] WebSocket Auth via JWT / WebSocket での JWT 認証

---

## ⚙️ Installation / インストール

### English
1. Clone the repository: `git clone https://github.com/gabepidoras/demoCHAT.git`
2. Configure `src/main/resources/application.properties` (Database & SMTP).
3. Build and run: `./mvnw spring-boot:run`

### 日本語
1. リポジトリをクローン: `git clone https://github.com/gabepidoras/demoCHAT.git`
2. `src/main/resources/application.properties` を設定 (DBとSMTP)。
3. 実行: `./mvnw spring-boot:run`
