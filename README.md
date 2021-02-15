# normalizeNumexp
数量表現・時間表現の規格化を行うツール

## Overview
normalizeNumexpは、日本語の文中に含まれる数量表現・時間表現を高速かつ正確に抽出、規格化するツールです。 

具体的な入力・出力例は以下のようになります。自然言語文の入力に対し、抽出した数量表現・時間表現とその規格化表現を表示します。

## Requirement
数量表現・時間表現のできるプログラムを探していたのですが、何年も前のプログラムで、C++で書かれていて、
JVM環境で使うためにはいろいろと問題もあり、リバースエンジニアリングでソースを作り直すことを始めました。


## Usage
基本的な仕組みについては[こちら](https://www.cl.ecei.tohoku.ac.jp/index.php?Open%20Resources%2FnormalizeNumexp%2FDetail)に詳しく載っています。
主にはC++からJAVAへの変換を行っております。

### Maven
ベースディレクトリから`mvn package`を実行します。 パッケージは `target`ディレクトリに組み込まれています。

### Eclipse
Quick start:
* Checkout code from github
* File menu > New Java Project
* Uncheck "Use default location"
* Set project layout to Use project folder as root for sources and class files
* Browse for location checked out code
* Hit next
* Hit finish
* Build!

## Features
C++ to Java Converterのツールを利用して、C++のソースをJAVAに一旦変換、その後、変換ミスを修正する手法をとる。
J-UNITを利用して、テストケースを網羅して、リバースエンジニアリングのミスをなくす。

## Reference

normalizeNumexp 数量表現・時間表現の規格化を行うツール<br>
http://www.cl.ecei.tohoku.ac.jp/index.php?Open%20Resources%2FnormalizeNumexp

C++ to Java Converter<br>
https://www.tangiblesoftwaresolutions.com/product_details/cplusplus_to_java_converter_details.html

## Author

[@blue_islands](https://twitter.com/blue_islands)

## Licence

BSD-3-Clause License
