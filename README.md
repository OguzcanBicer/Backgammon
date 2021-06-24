# Tavla konsol oyun uygulamasi



## Projem hakkında
![resim](https://user-images.githubusercontent.com/52275789/123220159-57704f00-d4d6-11eb-89a3-1bb84de5aaf2.png) 

 * İlk kimin başlayacağını öğrenmek için placement zarı atılır. (Zarlar aynı gelirse sistem otomatik olarak tekrar zar atar.)
 * Oyuncular ister zarın toplamını, ister tek tek zar oynayabilir. 
 * Oyun tahtası sıra kimdeyse ona göre dönmektedir.

![resim](https://user-images.githubusercontent.com/52275789/123219727-e466d880-d4d5-11eb-953b-caead5ec8ad1.png)

![resim](https://user-images.githubusercontent.com/52275789/123222459-a5865200-d4d8-11eb-9e5f-d198f26d1251.png) <br/>
Kırık taşlar "(#)" kolonunda saklanmaktadır. Kırık taşın varsa, kırık taşını oynamak zorundasın, # işaretiyle buradaki taşı oynayabilirsin. Eğer oynayamıyorsan geçerli hamlen yok deyip sırayı atlar.<br/>
Toplanan taşlar "0" hanesinde toplanır. 0 yazarak oynayabilirsin. Eğer oyuncu 15 tane toplanan taşa sahip olursa oyunu kazanır.<br/>


Projenin en önemli noktası, kayıt özelliği:
 * Atılan her zar text dosyasında kayıt altındadır. (**diceLog.txt**)
 * Oyun tahtasının şekli her tur hamlede kayıt altına alınmaktadır. (**Table.dat** // notepad ile açılabilmektedir.)

Oyunun her hamlesi kayıt altına alındığından oyuncu dilediği gibi programı kapatsa da, tekrar oyunu açtığında, oyun kaldığı yerden devam eder. 

Yeni oyuna başlamak istenirse bunun için 999 yazması yeterlidir. Oyun standart tavla başlangıcına döner ve kayıt dosyaları sıfırlanarak yeni oyun kayıt edilmeye başlar.

Devam edecek.
