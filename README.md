# normalize-numexp
数量表現・時間表現の規格化を行うツール

## 概要
「normalize-numexp」は、日本語の文章中に含まれる数量表現や時間表現を抽出し、規格化するツールです。自然言語文の入力に対して、抽出した数量表現・時間表現とその規格化表現を表示します。詳細は[こちら](http://www.cl.ecei.tohoku.ac.jp/index.php?Open%20Resources%2FnormalizeNumexp)を参照してください。

## リバースエンジニアリング
このツールは、[normalizeNumexp](http://www.cl.ecei.tohoku.ac.jp/index.php?Open%20Resources%2FnormalizeNumexp)というC++で書かれたツールをリバースエンジニアリングによりJavaに変換したものです。変換には[C++ to Java Converter](https://www.tangiblesoftwaresolutions.com/product_details/cplusplus_to_java_converter_details.html)を利用し、その後、変換ミスを修正します。また、J-UNITを利用してテストケースを網羅し、リバースエンジニアリングのミスを排除します。

## 必要条件
このツールを使用するためには、Java Virtual Machine (JVM) 環境が必要です。

## ダウンロード
Latest Version:
[![](https://jitpack.io/v/cotogoto/normalize-numexp.svg)](https://jitpack.io/#cotogoto/normalize-numexp)

下記の **VERSION** キーを上記の最新バージョンに必ず置き換えてください

Maven
```xml
<dependency>
    <groupId>com.github.cotogoto</groupId>
    <artifactId>normalize-numexp</artifactId>
    <version>VERSION</version>
</dependency>
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

## ライセンス
このプロジェクトはBSD-3-Clause Licenseの下でライセンスされています。

## 作者
このプロジェクトは@blue-islandsによって開発されました。

## Licence
BSD-3-Clause License
