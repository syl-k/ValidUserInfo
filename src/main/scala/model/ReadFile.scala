package model

import java.io.{FileInputStream, InputStreamReader, PrintWriter}
import java.text.SimpleDateFormat
import java.util.Date

import com.opencsv.CSVReader

import scala.collection.mutable.ArrayBuffer
import scala.util.control.Breaks._

/**
  * ファイル操作クラス.
  * Created by T_Kikuyama on 2016/10/24.
  */
class ReadFile {

    /**
      * ファイル読み込み.
      * @param fileName ファイル名
      */
    def operateFile(fileName: String): Unit = {
        val inputStream = new FileInputStream(fileName)
        val reader = new InputStreamReader(inputStream, "ms932")
        val csvReader = new CSVReader(reader, ',', '"')

        val csvHeader = csvReader.readNext()
        val columnNumList = getColumnNumList(csvHeader)
        val outputFileName = getOutputFileName(columnNumList.length)
        outputDataToCSV(csvReader, csvHeader, columnNumList, outputFileName)
    }

    /**
      * CSVファイルヘッダ確認.
      * @param line CSVファイルのヘッダーデータ
      * @return 特定のカラムの番号
      */
    def getColumnNumList(line: Array[String]): Array[Int] = {
        val columnNumList = ArrayBuffer.empty[Int]

        line.zipWithIndex.foreach { case(column, i) =>
            if (column == "メールアドレス" || column == "住所" || column == "電話番号" || column == "お届け先住所")
                columnNumList += i
        }
        columnNumList.toArray
    }

    /**
      * 出力ファイル名取得.
      * @param dataType 読み込みファイルデータ種別.
      * @return 出力ファイル名.
      */
    def getOutputFileName(dataType: Int): String = {
        val fileName = dataType match {
            case 1 => "メールアドレス不備リスト_"
            case 2 => "受注データ不備リスト_"
            case _ => "出力失敗_"
        }
        val outputDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date)
        fileName + outputDate + ".csv"
    }

    /**
      * ファイル出力.
      * @param csvReader CSVReader
      * @param csvHeader CSVファイルヘッダー
      * @param columnNumList カラム番号のリスト
      * @param outputFileName 出力ファイル名
      */
    def outputDataToCSV(csvReader: CSVReader, csvHeader:Array[String], columnNumList: Array[Int], outputFileName: String) = {
        val outFile = new PrintWriter(outputFileName, "Shift_JIS")
        outFile.write("%s,%s\n" format("不備箇所", csvHeader.mkString(",")))

        var outputLineCount: Int = 0
        breakable {
            while (true) {
                val line = csvReader.readNext()
                if (line == null) {
                    break
                }
                if (columnNumList.length == 1) {
                    outputLineCount += validMailAddressData(line, outFile, columnNumList)
                } else if (columnNumList.length == 2) {
                    outputLineCount += validateCSVData(line, outFile, columnNumList)
                }
            }
        }

        if (outputLineCount == 0) outFile.write("出力データなし")
        outFile.close()
    }

    /**
      * メールアドレス用CSVファイルデータのバリデーションチェック.
      * @param line CSVファイルの行データ
      * @param out  出力先ファイル
      * @param columnNumList カラム番号のリスト
      * @return Int 1: 出力データあり, 0: 出力データなし
      */
    def validMailAddressData(line: Array[String], out: PrintWriter, columnNumList: Array[Int]): Int = {
        val errorMes = ArrayBuffer.empty[String]

        val mailAddress: String = line(columnNumList(0))
        val isValidaMailAddress: Boolean = Validate.mailAddressFormat(mailAddress)
        if (!isValidaMailAddress) errorMes += "メールアドレス"

        if (errorMes.nonEmpty) {
            out.write("%s不備,%s\n" format(errorMes.toArray.mkString("_"), line.mkString(",")))
            1
        } else 0
    }

    /**
      * CSVファイルデータのバリデーションチェック.
      * @param line CSVファイルの行データ
      * @param out  出力先ファイル
      * @param columnNumList カラム番号のリスト
      * @return Int 1: 出力データあり, 0: 出力データなし
      */
    def validateCSVData(line: Array[String], out: PrintWriter, columnNumList: Array[Int]): Int = {
        if (line.length < 8) return 0

        val errorMes = ArrayBuffer.empty[String]

        val address: String = line(columnNumList(0))
        val isValidaAddress: Boolean = Validate.addressFormat(address)
        if (!isValidaAddress) errorMes += "住所"

        val phoneNum: String = line(columnNumList(1))
        val isValidPhoneNum: Boolean = Validate.phoneNumberFormat(phoneNum)
        if (!isValidPhoneNum) errorMes += "電話番号"

        if (errorMes.nonEmpty) {
            out.write("%s不備,%s\n" format(errorMes.toArray.mkString("_"), line.mkString(",")))
            1
        } else 0
    }
}
