# Java API仕様

## 適用範囲

本プロジェクトにHTTPエンドポイント、Controller、認証・権限、HTTPステータスコードはありません。この文書は、実装とテストから確認できる主要なJava呼び出しAPIとCLIを記録します。全publicクラスを安定した公開APIとして保証する文書ではありません。

## 統合API

### 構築

```java
NormalizeNumexp normalizer = new NormalizeNumexpImpl("ja");
```

- 実装: `jp.livlog.numexp.normalizeNumexp.impl.NormalizeNumexpImpl`
- コンストラクタ引数: `String language`
- 対応値: `ja`、`zh`
- Validation: nullは `NullPointerException`、空文字と未知言語は `IllegalArgumentException` です。

### `normalize(String text)`

- 戻り値: `List<String>`
- 概要: 入力から4種の表現を抽出し、数量、絶対時間、相対時間、持続時間の種別順で返します。各種別内は抽出順です。該当がなければ空のリストです。
- 文字列形式: `type*originalExpression*positionStart*positionEnd*counter*lowerbound*upperbound*options`
- `type`: `numerical`、`abstime`、`reltime`、`duration`
- `counter`: 数量では単位、時間系では `none`
- 相対時間: lower/upper内が絶対部分と相対部分のカンマ区切りです。
- Validation / error: nullは `NullPointerException` です。最大入力サイズは規定していません。辞書の欠落・読取失敗は `IllegalStateException` となり、不完全な辞書状態では処理を継続しません。

### `normalizeData(String text)`

- 戻り値: `List<jp.livlog.numexp.share.Expression>`
- 概要: `normalize` と同じ抽出を、公開フィールドを持つデータオブジェクトとして入力内の位置順で返します。

| フィールド | 内容 |
|---|---|
| `type` | 表現種別 |
| `originalExpression` | 入力中の元表現 |
| `positionStart`, `positionEnd` | 入力中の位置 |
| `counter` | 単位。時間系は `none` |
| `valueLowerbound`, `valueUpperbound` | 数量・絶対時間・持続時間の上下限 |
| `valueLowerboundAbs`, `valueUpperboundAbs` | 相対時間の絶対部分 |
| `valueLowerboundRel`, `valueUpperboundRel` | 相対時間の期間部分 |
| `options` | 修飾情報のカンマ区切り文字列 |

表現種別によって使わないフィールドは設定されず、nullのままになり得ます。`Expression` はLombok `@ToString` を持ちますが、equals/hashCodeや不変性は提供しません。

## CLI

エントリーポイントは `jp.livlog.numexp.Main` です。

```text
java jp.livlog.numexp.Main <language> <text>
```

- 引数が2個未満: 使用法を標準エラーへ表示し、終了コード2で終了します。
- 引数が2個以上: 先頭2個だけを使用し、各規格化文字列を標準出力へ出します。
- 正常終了コードは0です。未処理の実行時例外はJVMの非0終了となります。引用符、複数語テキストのシェル別起動手順は定義されていません。
- POMには実行用プラグインやfat JAR設定がないため、依存を含む具体的な起動コマンドは未整備です。

## 内部拡張API

数量・時間別の抽象 `*Normalizer`、`DigitUtility`、`NormalizerUtility` 等もpublicですが、主に実装内部で利用されます。互換性保証の記録がないため、外部利用を前提に変更可否を判断しないでください。主要な利用窓口は `NormalizeNumexp` / `NormalizeNumexpImpl` と見なせますが、正式な公開範囲は人間の確認が必要です。

## HTTP API項目

Method、Path、Request Body、HTTP Response、Status Code、Controller / Service、認証・権限は該当しません。将来HTTP層を追加する場合は、このJava APIと別の契約として文書化してください。

## 並行利用と入力サイズ

- `NormalizeNumexpImpl` インスタンスのスレッドセーフ性は保証しません。並行処理ではインスタンスを共有しないでください。
- 最大入力サイズは定めていません。呼び出し側で入力制限とタイムアウトを設けてください。ライブラリとしての推奨値は性能計測後に決定します。

## 未確認事項

- `positionEnd` が常に排他的終端であることの公開保証。
- バージョン間で互換性を維持するpublicクラスとフィールドの範囲。
