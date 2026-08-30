# アーキテクチャ

## 全体像

本プロジェクトは単一JARとして動く、ルール・辞書ベースのJavaライブラリです。Webサーバー、画面、DBは持ちません。Mavenは `src/` を本体、`test/` をテスト、`resources/` をclasspathリソースとして構成します。

処理の流れは次のとおりです。

1. `NormalizeNumexpImpl` が入力文字列を4種の normalizer へ渡す。
2. 各 normalizer が `NumberNormalizer` で数値候補を抽出・変換する。
3. `NormalizerTemplate` の共通処理と `resources/dic/{language}/` のパターンにより、数量・時間表現へ変換する。
4. `InappropriateExpressionRemoverImpl` が誤抽出候補を除外する。
5. `List<String>` は種別ごとの順（数量、絶対時間、相対時間、持続時間）、`List<Expression>` は出現位置順に並べて返す。

## 主要コンポーネント

- `normalizeNumexp`: 利用者向けの統合窓口。抽象クラス `NormalizeNumexp` と実装 `NormalizeNumexpImpl` がある。
- `numberNormalizer`: 半角・全角・漢数字、符号、小数、範囲などの抽出と数値変換を担う。
- `numericalExpressionNormalizer`: 数量、助数詞、SI単位、上下限等を処理する。
- `abstimeExpressionNormalizer`: 絶対日付・時刻を処理する。
- `reltimeExpressionNormalizer`: 「前」「後」等を含む相対時間を処理する。
- `durationExpressionNormalizer`: 時間量・期間を処理する。
- `normalizerTemplate` / `normalizerUtility`: 辞書ロード、パターン探索、修飾子や範囲処理の共通基盤。
- `inappropriateExpressionRemover`: 辞書やURL規則により誤抽出候補を削除する。
- `share`: `Expression`、`NNumber`、`NTime`、記号定数等の共有モデル。
- `resources/dic`: 実行時辞書。`ja/raw` には生成元データと生成スクリプトがある。

## 一般的なレイヤーとの対応

Controller / Service / Repository / Entity / DTO / View / Component というWeb/DBレイヤーは存在しません。`Main` は最小限のCLIエントリーポイント、`NormalizeNumexpImpl` はファサード兼オーケストレーター、各 normalizer はドメイン処理、`Expression` は返却用データモデルに相当します。Repositoryや永続Entityはありません。

## 外部連携と設定

実行時の外部API、ネットワーク、環境変数、設定ファイル、DB接続は確認されていません。辞書は `DictionaryResourceLoader` がclasspathから読みます。ビルド時のみMaven Central等から依存を解決し、POMにはJitPack向け配布設定があります。

## エラー処理とログ

- CLIの引数が2個未満なら、使用法を標準エラーへ表示し、終了コード2で終了します。正常時は0です。実行中の未処理例外はJVMの非0終了となります。
- 辞書は一時リストへ全行を読み切ってから反映します。読取中の `IOException` は辞書パスを含む `IllegalStateException` として再送出され、不完全な辞書状態では処理を継続しません。
- 一部の未対応文字変換は `NullPointerException` を送出します。
- 辞書ロードは `DictionaryResourceLoader` に集約されています。UTF-8で読み、要求言語のリソースがなければ日本語パスを試し、それもなければ解決後のパスを含む `IllegalStateException` を送出します。
- ライブラリ固有のログ実装やログ設定はありません。利用アプリケーションのログ構成へ干渉せず、Secretsや入力全文を新たにログ出力しないでください。

## 設計上の注意点

- 抽象クラス/APIと `impl` の分離、および `NormalizerTemplate` に集約された処理順を保つ。
- 4種の抽出結果は相互に重複し得るため、不適切表現除去と最終ソートまで含めて回帰確認する。
- `positionStart` / `positionEnd`、規格化文字列のアスタリスク区切り、種別名は利用者に見える契約として扱う。
- 辞書はコードと同等に挙動へ影響する。生成済みファイルだけを手編集するか、rawから再生成するかを変更前に確認する。
- 公開フィールドやC++移植由来の構造を、局所変更のついでに全面的にカプセル化・改名しない。
- 新規依存、公開API変更、辞書一括再生成、例外方針変更は事前に人間の判断を得る。
- normalizerは処理中に内部フィールドを利用するため、`NormalizeNumexpImpl` インスタンスの並行共有は保証しない。スレッドセーフ化は共有状態の除去と並行テストを伴う独立した変更として扱う。
- 最大入力サイズは計測前に固定しない。性能要件を定める場合は代表入力と極端な数字列について時間・メモリを測定する。
- 表現種別間の重複除去は位置区間の索引を使います。削除優先順を変更すると規格化結果が変わり得るため、最適化時も数量→相対時間→持続時間→絶対時間の既存順を維持します。
