package model

/**
  * データのバリデーション実行クラス.
  * Created by T_Kikuyama on 2016/10/24.
  */
object Validate {

    /**
      * 住所バリデーションチェック.
      * @param  address 住所
      * @return Boolean true: 正常値である, false: 不正値である
      */
    def addressFormat(address: String): Boolean = {
        val regex = """([ぁ-ん一-龠]{2,3}?[都道府県])(((?:旭川|伊達|石狩|盛岡|奥州|田村|南相馬|那須塩原|東村山|武蔵村山|羽村|十日町|上越|富山|野々市|大町|蒲郡|四日市|姫路|大和郡山|廿日市|下松|岩国|田川|大村|宮古|富良野|別府|佐伯|黒部|小諸|塩尻|玉野|周南)市|(?:余市|高市|[^市]{2,3}?)郡(?:玉村|大町|.{1,5}?)[町村]|(?:.{1,4}市)?[^町]{1,4}?区|.{1,7}?[市町村]))([ぁ-ん一-龠]{1,2}町[ぁ-ん一-龠ツヶケ]{1,6}|[ぁ-ん一-龠ツヶケ]{1,9}|)""".r

        val findPrefectures = regex.findPrefixOf(address)
        var splitAddress:Array[String] = null
        if (findPrefectures.isDefined) {
            splitAddress = address.split(findPrefectures.get)
        } else {
            return findPrefectures.isDefined
        }

        if (splitAddress.length < 2) return false

        val regex2 = """([0-9０-９一ニ二三四五六七]{1,6})(((―|-|‐|ｰ|ー|－|条|丁目|番|番地)([0-9０-９]{1,4})((―|-|‐|ｰ|ー|－|番|番地)([0-9０-９]{1,4})|([ぁ-ん一-龠ァ-タダ-ヶa-zａ-ｚA-ZＡ-Ｚー\s・Ⅰ\S]{1,18})([0-9０-９]{1,4})|)((([ぁ-ん一-龠ァ-タダ-ヶa-zａ-ｚA-ZＡ-Ｚー\s・Ⅰ\S]{1,18})([0-9０-９]{1,4})|((―|-|‐|ｰ|ー|－|)([0-9０-９]{1,4})|)|)(([ぁ-ん一-龠ァ-タダ-ヶa-zａ-ｚA-ZＡ-Ｚー\s・Ⅰ\S]{1,18})([0-9０-９]{1,4})((―|-|‐|ｰ|ー|－|)([0-9０-９]{1,4})|)|(―|-|‐|ｰ|ー|－|)([0-9０-９]{1,4})|)|))|([ぁ-ん一-龠ァ-タダ-ヶa-zａ-ｚA-ZＡ-Ｚー\s・Ⅰ\S]{1,18})([0-9０-９]{1,4})|)(号|号室|番地|F|)"""

        splitAddress(1) matches regex2
    }

    /**
      * メールアドレスバリデーションチェック.
      * @param mailAddress メールアドレス
      * @return true: 正常値である, false:不正値である
      */
    def mailAddressFormat(mailAddress: String): Boolean = {
        val regex = """[a-zA-Z0-9!$&*.=^`|~#%'+\/?_{}-]+@([a-zA-Z0-9_-]+\.)+[a-zA-Z]{2,4}$""".r
        regex.findFirstIn(mailAddress).isDefined
    }

    /**
      * 電話番号バリデーションチェック.
      * @param phoneNumber 電話番号
      * @return true: 正常値である, false: 不正値である
      */
    def phoneNumberFormat(phoneNumber: String): Boolean = {
        val regex = """(0[5789]0-[0-9]{4}-[0-9]{4})|0([1-9]{1,3}-[0-9]{2,4})-[0-9]{4}""".r
        regex.findFirstIn(phoneNumber).isDefined
    }
}