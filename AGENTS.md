# AGENTS.md

このファイルはリポジトリ全体に適用する、AI コーディングエージェント向けの共通指示です。下位ディレクトリに別の `AGENTS.md` が追加された場合は、その配下では対象に近いファイルの指示を優先してください。

## Project Overview

- 日本語（および一部中国語）の文章から数量・絶対時間・相対時間・持続時間表現を抽出し、ルールと辞書で規格化する Java ライブラリです。
- Java 17 / Maven の JAR プロジェクトです。主要依存は Gson、Lombok、テストは JUnit 5（既存assertionのためJUnit 4も併用）です。
- 標準 Maven 配置ではありません。実装は `src/jp/livlog/numexp/`、テストは `test/jp/livlog/numexp/`、実行時辞書は `resources/dic/{ja,zh}/` にあります。
- `*Normalizer` が処理の抽象/API、`impl/*Impl` が実装、`normalizerTemplate` と `normalizerUtility` が共通処理、`share` が共通データ型を担います。Web/DB レイヤーや開発サーバーはありません。

## Commands

- 前提確認: `java -version` と `mvn -version`（コンパイラ設定は Java 17）。Maven Wrapper はないため、Maven を別途用意します。
- 依存解決とコンパイル: `mvn compile`
- ビルド: `mvn package`（テストを実行し、成果物を `target/` に作成）
- テスト: `mvn test`
- lint / format / 独立した typecheck / DB migration: 設定・コマンドはありません。存在しないツールを前提にしないでください。

## Code Style

- 既存の Java 形式（4 スペース、波括弧は同じ行、型・クラスは PascalCase、メソッド・変数は camelCase、定数は UPPER_SNAKE_CASE）と既存パッケージ構成に合わせ、無関係な整形を混ぜないでください。
- 公開抽象クラス/インターフェースと `impl` 実装の分離、`NormalizerTemplate` による共通処理、`Expression` 系データ型という既存パターンを保ちます。C++ から変換された由来の命名や公開フィールドを、作業範囲外で一括修正しないでください。
- 辞書変更では `resources/dic/` の生成済み JSON 風テキストと `resources/dic/ja/raw/` の生成元・スクリプトの関係を先に確認し、文字コード、キー、表現順、言語別挙動を保持してください。README の古い `src/dic` 表記を根拠に新規配置しないでください。

## Testing

- JUnit 5 テストは本体と対応するパッケージで `test/` に置かれ、クラス名は `*Test` です。Mockito、fixture ディレクトリ、外部サービス用 mock はありません。
- 実装変更時は同じ normalizer の境界値・日本語表記・位置情報・上下限を既存 assertion の形式で追加し、辞書変更時は関連する数量/時間種別と不適切表現除去への回帰も確認してください。
- 実装後は対象テストに加えて `mvn test` を実行し、リリース可能性に関わる変更では `mvn package` まで確認してください。CLI の手動確認を行う場合は `Main` が `<language> <text>` の2引数を取ることと、辞書リソースが classpath にあることを確認します。

## Git Workflow

- リポジトリ固有のブランチ命名・通常コミット規約は確認できません。既存履歴から推測して確定せず、依頼者またはホスティング側の規約に従ってください。
- PR 前に差分を限定し、ビルド結果、実行できた/スキップされたテスト、辞書や公開出力形式への影響を記載します。挙動や導入方法が変わる場合だけ `README.md` / `detail.md` を同じ変更で更新します。
- `release.properties`、`pom.xml.releaseBackup`、バージョン、SCM タグ、release/deploy 操作はリリース管理対象です。明示的な依頼なしに変更・実行しないでください。

## Boundaries

- `.env*`、Secrets、API キー、パスワード、個人情報を読み取り・表示・コミットしないでください。現時点で環境変数、外部 API、DB、DDL、migration、Docker、CI 設定は確認されていません。将来追加された場合も値や本番接続を推測しません。
- `target/`、IDE 設定、バックアップファイルを成果物として編集・コミットしません。大規模リファクタリング、公開 API/規格化出力の変更、新規依存、POM のテスト設定変更、辞書の一括再生成は事前確認が必要です。
- 対応言語は `ja` と `zh` です。`NormalizeNumexpImpl` のインスタンス共有はスレッドセーフ性を保証していないため避け、処理単位またはスレッド単位で生成してください。計測根拠のない最大入力サイズをライブラリ仕様として追加せず、利用側で入力上限とタイムアウトを設けてください。

## Workflow

- 作業前に `README.md`、詳細仕様なら `detail.md`、ビルドなら `pom.xml`、対象実装と対応テスト、辞書変更なら該当する `resources/dic/` と生成元を読みます。
- 変更前に呼び出し元、抽象/API と `impl`、関連する4種の normalizer、辞書ロード経路を検索し、最小差分にします。
- 実装後は上記 Commands と Testing に従って確認し、未実行・スキップ・手動確認を明記します。公開出力、曖昧表現の意味、リリース、本番、Secrets、テスト有効化など運用判断が必要なら推測せず人間に確認してください。
- 文書の役割を分けます。`README.md` は人間向けの導入と利用案内、`AGENTS.md` はリポジトリ全体の共通作業指示、Steering は複数プロダクトで反復利用する規則、`docs/specs/{issue-number}/ai-instructions.md` は当該 Issue だけの変更範囲・禁止事項・確認方法です。機能を継続更新する場合は機能単位の `current-spec.md` と `change-log.md` を優先し、同じ内容を複製しないでください。
