# normalizeNumexp
数量表現・時間表現の規格化を行うツール

## 概要
「normalize-numexp」は、日本語の文章中に含まれる数量表現や時間表現を抽出し、規格化するツールです。自然言語文の入力に対して、抽出した数量表現・時間表現とその規格化表現を表示します。詳細は[こちら](http://www.cl.ecei.tohoku.ac.jp/index.php?Open%20Resources%2FnormalizeNumexp)を参照してください。

## リバースエンジニアリング
このツールは、[normalizeNumexp](http://www.cl.ecei.tohoku.ac.jp/index.php?Open%20Resources%2FnormalizeNumexp)というC++で書かれたツールをリバースエンジニアリングによりJavaに変換したものです。変換には[C++ to Java Converter](https://www.tangiblesoftwaresolutions.com/product_details/cplusplus_to_java_converter_details.html)を利用し、その後、変換ミスを修正します。また、J-UNITを利用してテストケースを網羅し、リバースエンジニアリングのミスを排除します。

## 必要条件
このツールを使用するためには、Java Virtual Machine (JVM) 環境が必要です。

## 使用方法
このツールの使用方法は以下の通りです。

1. Mavenを使用する場合: ベースディレクトリから`mvn package`を実行します。パッケージは targetディレクトリに組み込まれています。
2. Eclipseを使用する場合: 
   - GitHubからコードをチェックアウトします。
   - ファイルメニューから新規Javaプロジェクトを作成します。
   - "Use default location"のチェックを外します。
   - プロジェクトレイアウトを"Use project folder as root for sources and class files"に設定します。
   - チェックアウトしたコードの場所を参照します。
   - 次へをクリックします。
   - 完了をクリックします。
   - ビルドします。

## ライセンス
このプロジェクトはBSD-3-Clause Licenseの下でライセンスされています。

## 作者
このプロジェクトは@blue_islandsによって開発されました。

## Licence
BSD-3-Clause License
