# 传输

在整个"数据"中，并不是所有部分都是数据包

数据有以下组成部分：

首先读取4个字节，使用Base256 int转为数字，这是整个数据包的长度，随后读取后面所有数据进行下一步处理

得到完整的数据后，读取2个字节，使用Base256 tag转为数字，这是压缩算法的ID，随后读取这个数据剩余的所有数据后进行解压

得到解压后的数据，有以下组成：

| 长度 |  1字节   |    x字节    |    1字节    |    x字节    |     2字节     |    x字节     |   16字节   |       2~9字节        |       2~9字节        |    16字节     |      x字节      |
|:--:|:------:|:---------:|:---------:|:---------:|:-----------:|:----------:|:--------:|:------------------:|:------------------:|:-----------:|:-------------:|
| 用途 | 摘要数据长度 |   摘要数据    | 使用的密钥名称长度 |  使用的密钥名称  |    签名长度     |    签名数据    | 随机标识符数据  |       时间戳数据        |       数据包ID        |    回执标识符    |     数据包主体     |
| 类型 |  byte  |  byte[]   |   byte    |  byte[]   |   byte[]    |   byte[]   |  byte[]  |       byte[]       |       byte[]       |   byte[]    |    byte[]     |
| 注释 |        | x为上一个数据的值 |           | x为上一个数据的值 | 使用Base256编码 | 用于验证发送者的签名 | 用以抵抗重放攻击 | 使用SkippedBase256编码 | 使用SkippedBase256编码 | 用以对特定消息进行回复 | x为后续所有字节，不定长度 |

数据包的组成是按照类中字段顺序的，通过类签名获得类型，因此数据中很少存在用以指定类型的数据

在读取"数据包主体"的x字节后，进行进一步读取，请参考本目录下的详细数据包格式信息

