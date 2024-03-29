/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50725
Source Host           : localhost:3306
Source Database       : movies

Target Server Type    : MYSQL
Target Server Version : 50725
File Encoding         : 65001

Date: 2021-06-04 09:24:14
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_sys_feedback
-- ----------------------------
DROP TABLE IF EXISTS `tb_sys_feedback`;
CREATE TABLE `tb_sys_feedback` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '网站反馈意见表',
  `user_id` bigint(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `feedback` varchar(255) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_sys_feedback
-- ----------------------------
INSERT INTO `tb_sys_feedback` VALUES ('1', '1', '网站优美', '该网站的美化工作做得很好很喜欢。', '2021-05-07 16:20:16');
INSERT INTO `tb_sys_feedback` VALUES ('2', '2', '网站功能完善', '该网站的功能完善，设计很人性化。', '2021-05-07 16:21:49');
INSERT INTO `tb_sys_feedback` VALUES ('3', '3', '电影丰富', '网站的电影信息丰富，很喜欢', '2021-05-07 16:22:42');
INSERT INTO `tb_sys_feedback` VALUES ('4', '2', '7', '7', '2021-05-26 22:56:12');
INSERT INTO `tb_sys_feedback` VALUES ('5', '4', '5', '5', '2021-05-26 22:56:15');

-- ----------------------------
-- Table structure for tb_sys_film
-- ----------------------------
DROP TABLE IF EXISTS `tb_sys_film`;
CREATE TABLE `tb_sys_film` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '电影名',
  `category_id` bigint(11) NOT NULL COMMENT '电影类型ID',
  `img` varchar(255) NOT NULL,
  `director` varchar(255) DEFAULT NULL COMMENT '导演',
  `producer` varchar(255) DEFAULT NULL COMMENT '主演1',
  `country` varchar(255) DEFAULT NULL,
  `actors` varchar(255) NOT NULL COMMENT '主演2',
  `language` varchar(255) DEFAULT '' COMMENT '语言',
  `year` varchar(255) DEFAULT NULL COMMENT '上映年份',
  `length` int(255) DEFAULT NULL COMMENT '返回列表时保存类别名称',
  `introduce` varchar(500) DEFAULT NULL COMMENT '冗余字段',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=120 DEFAULT CHARSET=utf8 COMMENT='电影表';

-- ----------------------------
-- Records of tb_sys_film
-- ----------------------------
INSERT INTO `tb_sys_film` VALUES ('1', '哪吒之魔童降世', '1', '/static/movies/images/nzmtjs.jpg', '饺子', '饺子/易巧 ', '中国', '吕艳婷/囧森瑟夫/瀚墨', '汉语普通话', '2019', '110', '天地灵气孕育出一颗能量巨大的混元珠，元始天尊将混元珠提炼成灵珠和魔丸，灵珠投胎为人，助周伐纣时可堪大用；而魔丸则会诞出魔王，为祸人间。元始天尊启动了天劫咒语，3年后天雷将会降临，摧毁魔丸。太乙受命将灵珠托生于陈塘关李靖家的儿子哪吒身上。然而阴差阳错，灵珠和魔丸竟然被掉包。本应是灵珠英雄的哪吒却成了混世大魔王。调皮捣蛋顽劣不堪的哪吒却徒有一颗做英雄的心。然而面对众人对魔丸的误解和即将来临的天雷的降临，哪吒是否命中注定会立地成魔？他将何去何从？', '2021-05-14 10:10:39', '2021-05-14 17:49:09');
INSERT INTO `tb_sys_film` VALUES ('2', '唐人街探案', '1', '/static/movies/images/trjta.jpg', '陈思诚', '陈思诚', '中国', '王宝强/刘昊然/妻夫木聪/托尼·贾 ', '汉语普通话', '2021', '136', '继曼谷、纽约之后，东京再出大案。唐人街神探唐仁（王宝强 饰）、秦风（刘昊然 饰）受侦探野田昊（妻夫木聪 饰）的邀请前往破案。“CRIMASTER世界侦探排行榜”中的侦探们闻讯后也齐聚东京，加入挑战，而排名第一Q的现身，让这个大案更加扑朔迷离，一场亚洲最强神探之间的较量即将爆笑展开……', '2021-05-02 11:16:07', '2021-05-30 13:22:37');
INSERT INTO `tb_sys_film` VALUES ('3', '少年的你', '1', '/static/movies/images/sndn.jpg', '曾国祥', '林咏琛', '中国', '周冬雨/易烊千玺/尹昉/周也', '汉语普通话', '2019', '135', '陈念（周冬雨 饰）是一名即将参加高考的高三学生，同校女生胡晓蝶（张艺凡 饰）的跳楼自杀让她的生活陷入了困顿之中。胡晓蝶死后，陈念遭到了以魏莱（周也 饰）为首的三人组的霸凌，魏莱虽然表面上看来是乖巧的优等生，实际上却心思毒辣，胡晓蝶的死和她有着千丝万缕的联系。一次偶然中，陈念邂逅了名为小北（易烊千玺 饰）的小混混，随着时间的推移，心心相惜的两人之间产生了真挚的感情。小北答应陈念在暗中保护她免受魏莱的欺凌，没想到这一决定引发了一连串的连锁反应。负责调查胡晓蝶死因的警官郑易（尹昉 饰）隐隐察觉到校园里的古怪气氛，可他的调查却屡屡遭到校方的阻挠。', '2021-05-02 11:16:09', null);
INSERT INTO `tb_sys_film` VALUES ('4', '送你一朵小红花', '1', '/static/movies/images/snydxhh.jpg', '韩延', '韩今谅', '中国', '易烊千玺/刘浩存/朱媛媛/高亚麟', '汉语普通话', '2020', '128', '两个抗癌家庭，两组生活轨迹。影片讲述了一个温情的现实故事，思考和直面了每一个普通人都会面临的终极问题——想象死亡随时可能到来，我们唯一要做的就是爱和珍惜。', '2021-05-02 11:18:55', null);
INSERT INTO `tb_sys_film` VALUES ('5', '冰雪奇缘2', '2', '/static/movies/images/bxqy.jpg', '克里斯·巴克', '珍妮弗·李', '美国', '克里斯汀·贝尔/伊迪娜·门泽尔', '英语', '2019', '103', '历经严酷考验，阿伦戴尔王国终于回归往日平静。艾莎女王（伊迪娜·门泽尔 Idina Menzel 配音）、安娜公主（克里斯汀·贝尔 Kristen Bell 配音）以及他们的好友雪宝（乔什·加德 Josh Gad 配音）、克里斯托弗（乔纳森·格罗夫 Jonathan Groff 配音）、驯鹿斯文过着平静安逸的生活。可是最近一段时间，艾莎总会被一段神秘的吟唱所困扰，为了追寻真相，她义无反顾踏上了征途。担心姐姐的安全，安娜和雪宝、克里斯托弗他们紧紧跟随。在那座常年被浓雾所笼罩的森林里，不仅藏着神秘的自然力量，更隐藏着关于阿伦戴尔王国、艾莎的魔法来源以及两位公主父母丧生等一系列的秘密。', '2021-05-02 11:19:07', null);
INSERT INTO `tb_sys_film` VALUES ('6', '星际穿越 Interstellar', '2', '/static/movies/images/xjcy.jpg', '克里斯托弗·诺兰', '乔纳森·诺兰', '美国', '马修·麦康纳/安妮·海瑟薇', '英语', '2014', '169', '近未来的地球黄沙遍野，小麦、秋葵等基础农作物相继因枯萎病灭绝，人类不再像从前那样仰望星空，放纵想象力和灵感的迸发，而是每日在沙尘暴的肆虐下倒数着所剩不多的光景。在家务农的前NASA宇航员库珀（马修·麦康纳 Matthew McConaughey 饰）接连在女儿墨菲（麦肯吉·弗依 Mackenzie Foy 饰）的书房发现奇怪的重力场现象，随即得知在某个未知区域内前NASA成员仍秘密进行一个拯救人类的计划。多年以前土星附近出现神秘虫洞，NASA借机将数名宇航员派遣到遥远的星系寻找适合居住的星球。', '2021-05-02 11:19:08', null);
INSERT INTO `tb_sys_film` VALUES ('7', '权力的游戏', '2', '/static/movies/images/qldyx.jpg', 'D·B·威斯', '戴维·贝尼奥夫', '美国', '彼特·丁拉基/琳娜·海蒂', '英语', '2014', '60', '镜头转向北方，野人的大举进攻让守护长城的百余名守夜人人人自危，在琼恩（基特·哈灵顿 Kit Harington 饰）的带领下，他们能够平安度过这一场危机吗？越过海峡，“龙母”丹尼莉丝（艾米莉亚·克拉克 Emilia Clarke 饰）率领着她的无垢者军团一路前行，解放奴隶无数，一边是茫茫的大海，一边是越来越狂野的巨龙，丹尼莉丝最终能否实现自己的复辟梦想？', '2021-05-02 11:19:09', null);
INSERT INTO `tb_sys_film` VALUES ('8', '信条 Tenet ', '2', '/static/movies/images/xt.jpg', '克里斯托弗·诺兰', '克里斯托弗·诺兰', '美国', '约翰·大卫·华盛顿/罗伯特·帕丁森', '英语', '2020', '150', '世界存亡危在旦夕，“信条”一词是唯一的线索与武器。主人公穿梭于全球各地，开展特工活动，力求揭示“信条”之谜，并完成一项超越了真实时间的神秘任务。这项任务并非时间之旅，而是【时空逆转】。', '2021-05-02 11:19:10', null);
INSERT INTO `tb_sys_film` VALUES ('9', '寄生虫 기생충', '3', '/static/movies/images/jsc.jpg', '奉俊昊', '韩进元', '韩国', '宋康昊/李善均/曹如晶', '韩语', '2019', '132', '基宇（崔宇植 饰）出生在一个贫穷的家庭之中，和妹妹基婷（朴素丹 饰）以及父母在狭窄的地下室里过着相依为命的日子。一天，基宇的同学上门拜访，他告诉基宇，自己在一个有钱人家里给他们的女儿做家教，太太是一个头脑简单出手又阔绰的女人，因为自己要出国留学，所以将家教的职位暂时转交给基宇。就这样，基宇来到了朴社长（李善均 饰）家中，并且见到了他的太太（赵汝贞 饰），没过多久，基宇的妹妹和父母也如同寄生虫一般的进入了朴社长家里工作。然而，他们的野心并没有止步于此，基宇更是和大小姐坠入了爱河。随着时间的推移，朴社长家里隐藏的秘密渐渐浮出了水面。', '2021-05-02 11:19:11', null);
INSERT INTO `tb_sys_film` VALUES ('10', '狼少年', '3', '/static/movies/images/lsn.jpg', '赵圣熙', '赵圣熙', '韩国', '宋仲基/朴宝英/柳演锡', '韩语', '2012', '124', '因为体弱多病的顺伊（朴宝英 Bo-yeong Park 饰）需要疗养的缘故，顺伊母女三人从首尔搬家到了乡下的一栋别墅生活。而收拾屋子的时候顺伊意外的在仓库发现了神秘的前户主朴博士在这里遗留下了的狼孩（宋仲基 Joong-ki Song 饰）。顺伊妈妈出于善心收留了这个狼性未泯的男孩，并给他起名叫哲秀，就这样四个人开始了同住一个屋檐下的生活。顺伊起初反感这个不会说话，吃饭狼吞虎咽的闯入者，然而朝夕相处让顺伊萌生了照顾训练哲秀的念头。她开始教给他吃饭，读书，认字，还弹奏吉他给狼孩听，而哲秀对顺伊温暖的关怀十分受用，认真的做着顺伊教授给自己的东西。孤独的顺伊因为哲秀渐渐融入到自己的世界中而开心起来，对他产生了别样的爱恋。但是这份美好却被对狼孩虎视眈眈的智泰（刘延锡 Yeon-Seok Yoo 饰）打破，他一门心思的集结挑拨多方势力来捕杀哲秀。顺伊为了保护哲秀，不得不面临着与所爱的人分离的现实。', '2021-05-02 11:19:12', null);
INSERT INTO `tb_sys_film` VALUES ('11', '7号房的礼物', '3', '/static/movies/images/7hfdlw.jpg', '李焕庆', '刘英雅', '韩国', '柳承龙/朴信惠/郑镇荣/金正泰', '韩语', '2013', '127', '1997年，只有6岁儿童智商的智障男子李龙久（柳成龙 饰）和可爱的女儿艺胜（葛素媛 饰）相依为命，生活虽然简单清贫，却充满幸福。某天，执着为女儿买美少女战士书包的龙久意外卷入一起幼童诱拐奸杀案，而死者竟是警察局长的女儿。龙久懵懂无知，搞不清状况，昏头昏脑就被投入监狱。在7号牢房中，聚集着走私犯蘇杨浩（吴达洙 饰）、诈骗犯崔春浩（朴元尚 饰）、通奸犯姜万范（金正泰 饰）、恐吓犯老徐（金基灿 饰）和抢劫犯申奉植（郑万植 饰）等五毒俱全的“社会渣滓”。龙久孩子般纯洁的心渐渐感动了这几个“大坏蛋”，他们甚至不惜冒险将艺胜带入牢房与父亲相会。', '2021-05-02 11:19:13', null);
INSERT INTO `tb_sys_film` VALUES ('12', '82年生的金智英', '3', '/static/movies/images/82nsdjzy.jpg', '金度英', '刘英雅', '韩国', '郑有美/孔刘/金美京/孔敏晶', '韩语', '2019', '118', '表面看来，金智英（郑有美 饰）是一个生活在幸福之中的家庭主妇，有一个收入不菲又温柔体贴的丈夫郑大贤（孔侑 饰）和一个非常可爱的女儿，在波澜不惊的日子里享受着每一天的平淡和安稳。实际上，金智英的内心早就如同地震一般的产生了天崩地裂的动摇，在她的身上背负着无比沉重的枷锁。婆婆面前，她要扮演逆来顺受的好儿媳。丈夫面前，她又是无条件支持他的贤妻。面对女儿，金智英必须隐藏起脆弱，做她无坚不摧的母亲。那么，真正的金智英究竟在哪儿呢？长久的压抑和挣扎中，金智英患上了心理疾病，郑大贤决定和妻子一起接受心理咨询，共同面对命运设置给他们的难题。', '2021-05-02 11:19:13', null);
INSERT INTO `tb_sys_film` VALUES ('13', '天气之子', '4', '/static/movies/images/tqzz.jpg', '新海诚', '新海诚', '日本', '醍醐虎汰朗/森七菜/小栗旬', '日语', '2019', '112', '高一那年夏天，帆高（醍醐虎汰朗配音）离开位在离岛的家乡，独自一人来到东京，拮据的生活迫使他不得不找份工作，最后来到一间专门出版奇怪超自然刊物的出版社担任写手。不久，东京开始下起连日大雨，仿佛暗示着帆高不顺遂的未来，在这座繁忙城市里到处取材的帆高邂逅了与弟弟相依为命，不可思议的美少女阳菜（森七菜配音）。「等等就会放晴了喔。」阳菜这样告诉着帆高，不久，头顶的乌云逐渐散去，耀眼的阳光洒落街道……原来，阳菜拥有「改变天气」的奇妙能力……', '2021-05-02 11:19:14', null);
INSERT INTO `tb_sys_film` VALUES ('14', '恋途未卜', '4', '/static/movies/images/ltwb.jpg', ' 三木孝浩', '米内山陽子', '日本', '滨边美波/北村匠海/福本莉子', '日语', '2020', '124', '根据同名少女漫画改编，描绘了4名高中生男女的青春爱情故事。山本朱里（滨边美波）是崇尚现实恋爱的女主角，山本理央（北村匠海）则是她没有血缘关系的弟弟。', '2021-05-02 11:19:15', null);
INSERT INTO `tb_sys_film` VALUES ('15', '初恋', '4', '/static/movies/images/cl.jpg', '三池崇史', '中村雅', '日本', '洼田正孝/大森南朋/染谷将太', '日语', '2019', '108', '本片设定在东京的一晚，一名时运耗尽的拳击名手利奥(洼田正孝饰)遇到了他的初恋莫妮卡(小西樱子饰)。莫妮卡是一名吸毒的应召女郎，但她依旧很天真。莫妮卡不知不觉陷入毒品走私计划之中，两人在这晚被腐败的警察、黑帮、利奥的对手以及三合会派来的女杀手追杀。', '2021-05-02 11:19:15', null);
INSERT INTO `tb_sys_film` VALUES ('16', '萤火之森', '4', '/static/movies/images/yhzs.jpg', '大森贵弘', '绿川幸', '日本', '佐仓绫音/内山昂辉/辻亲八', '日语', '2011', '45', '某年夏天，6岁小女孩竹川萤来到爷爷家度假，她闯进了传说住满妖怪的山神森林。正当她因为迷路而焦急万分的时候，一个戴着狐狸面具的大男孩出现在她面前，并引领着萤找到回家的路。虽然萤分外感激，可是男孩却禁止她碰触自己的身体，原来这名叫银的男孩并非人类，他一旦被人类碰触就会烟消云散。在此后的日子里，萤和银成为好朋友，他们走遍了森林的每一个角落玩耍。日复一日，年复一年，每到夏天的时候萤就会如约来到森林和好朋友见面。她遵守着和银的约定，无论如何也不碰触银的身体。随着年龄的增长，萤和银对彼此的情感都悄悄发生了变化，他们共同期待相聚的日子，共同期待拥抱对方……', '2021-05-02 11:19:16', null);
INSERT INTO `tb_sys_film` VALUES ('17', '半个喜剧', '5', '/static/movies/images/bgxj.jpg', '周申', '刘露', '中国', '任素汐/吴昱翰/刘迅/汤敏', '汉语普通话', '2019', '111', '三个自由浪漫的年轻人，过着各怀心思的人生：有人急着摆脱单身，有人想在结婚前放荡一番，有人想在 大城市站稳脚跟。因为一次情感出轨，三人扭结成了一团“嬉笑怒骂”的乱麻。当各种价值观碰撞在一起， 当一个人需要平衡亲情友情与爱情......他们慌乱的生活，就像是半个喜剧。', '2021-05-02 11:19:17', null);
INSERT INTO `tb_sys_film` VALUES ('18', '囧妈', '5', '/static/movies/images/jm.jpg', '徐峥', '何可可', '中国', '徐峥/黄梅莹/袁泉/郭京飞', '汉语普通话', '2020', '126', '虽然有着诸多不舍，但是强势的老板徐伊万（徐峥 饰）还是和合作伙伴兼妻子张璐（袁泉 饰）结束了失败的婚姻。由于某种心理作祟，他试图阻挠前妻在海外重新创业。为了第一时间赶去美国，他回到母亲卢小花（黄梅莹 饰）的住处取护照，结果阴差阳错和母亲坐上了开往俄罗斯的K3次列车。如同身边的老年人一样，卢小花对儿子有着无休止的过度关爱与碎碎念。漫长的旅途中，伊万饱受折磨，他一边遥控表弟郭贴（郭京飞 饰）破坏张璐的生意，一边和母亲展开斗智斗勇的拉锯战。在这一过程中，母子二人冲突不断。伊万渐渐了解到母亲的爱情，同时他也开始反省自己的人生。', '2021-05-02 11:19:18', null);
INSERT INTO `tb_sys_film` VALUES ('19', '沐浴之王', '5', '/static/movies/images/myzw.jpg', '易小星', '张少初', '中国', '彭昱畅/乔杉/卜冠今/苇青', '汉语普通话', '2020', '103', '在一次搓澡服务中，富二代肖翔（彭昱畅 饰）和搓澡工周东海（乔杉 饰）发生矛盾，让周东海面临生活困境。肖翔因跳伞事故被送到医院记忆全失，周东海恰巧撞见，心生一计，骗肖翔是自己的弟弟并骗回周家澡堂当搓澡工，于是一个富二代开始了一段终身难忘的搓澡生涯……', '2021-05-02 11:19:18', null);
INSERT INTO `tb_sys_film` VALUES ('20', '一点就到家', '5', '/static/movies/images/ydjdj.jpg', '许宏宇', '张冀', '中国', '刘昊然/彭昱畅/尹昉/谭卓', '汉语普通话', '2020', '97', '魏晋北（刘昊然 饰）、彭秀兵（彭昱畅 饰）、李绍群（尹昉 饰）三个性格迥异的年轻人从大城市回到云南千年古寨，机缘巧合下共同创业，与古寨“格格不入”的他们用真诚改变了所有人，开启了一段非常疯狂、纯真的梦想之旅。', '2021-05-02 11:19:19', null);
INSERT INTO `tb_sys_film` VALUES ('21', '真爱至上', '6', '/static/movies/images/zazs.jpg', '理查德·柯蒂斯', '理查德·柯蒂斯', '美国', '休·格兰特/科林·费尔斯', '英语', '2003', '135', '失去母亲的小男孩终日郁郁寡欢，继父不知该如何安慰。当他得知继子喜欢上学校里最美的女孩，便热烈地鼓励儿子去追。小男孩为了赢得小女孩的关注，废寝忘食地练习架子鼓，只为了在圣诞节的晚会上能与她同台演出。勇敢的他，甚至为了最后的一记道别潇 洒地突破机场的安检区。两个孩子的母亲面对了婚姻危机。丈夫感情出轨，把项链送给了别的女人，给她的，则仅是一张她热爱的女歌手的CD。她找了借口仓皇躲入卧室，在歌声中隐忍啜泣。转个身，又夸张地大笑着，迎向娇女爱子欢喜的面容。 丈夫最终幡然醒悟。...... 如此的小故事共有十出，温暖你我的心。', '2021-05-02 11:19:20', null);
INSERT INTO `tb_sys_film` VALUES ('22', '假如爱有天意 ', '6', '/static/movies/images/jrayty.jpg', '郭在容', '郭在容', '韩国', '孙艺珍/曹承佑/赵寅成/李己雨 ', '韩语', '2013', '133', '2003年的大学生梓希（孙艺珍饰）一天收拾房间，无意中发现一个神秘的箱子，里面满载着母亲珠喜（孙艺珍饰）留下的情书，在阅读中她重温着母亲的初恋回忆。1968年的珠喜，是一个清纯可爱的少女，与穷学生俊河（曹承佑饰）一见钟情，由于珠喜显赫的家世，令两人的恋爱陷入俗套的门不当户不对的痛苦纠结中。2003年的梓希爱情同样坎坷，她暗恋着戏剧学会的尚民（赵寅成饰），却一直难以表白自己的爱意。而巧妙的是，梓希发觉母亲的初恋故事跟她自己遭遇十分相似。自己能得到幸福吗？还是如母亲一般曲折难求？', '2021-05-02 11:19:20', null);
INSERT INTO `tb_sys_film` VALUES ('23', '我的少女时代', '6', '/static/movies/images/wdsnsd.jpg', '陈玉珊', '曾咏婷', '中国', '宋芸桦/王大陆/李玉玺/刘德华', '汉语普通话', '2015', '134', '成功白领林真心（陈乔恩 饰）因被上司压迫、下属吐槽，陷入了少女时代的深深回忆。原来曾是平凡少女（宋芸桦 饰）的真心有着一段爆笑却有充满甜蜜的初恋回忆。少女真心曾经暗恋校草欧阳非凡（李玉玺 饰），却总是不敌校花陶敏敏（简延芮 饰）的魅力，令她苦恼不已，一次意外却让她与校霸徐太宇（王大陆 饰）组成了“失恋阵线联盟”，他们势要夺爱，上演了一幕幕生猛、搞笑的青春趣事，而在相处中两人的情感也发生了微妙的变化……若干年后，来到成人世界的林真心又能否重拾初心呢？', '2021-05-02 11:19:21', null);
INSERT INTO `tb_sys_film` VALUES ('24', '听说', '6', '/static/movies/images/ts.jpg', '郑芬芬', '郑芬芬', '中国', '彭于晏/陈意涵/陈妍希', '汉语普通话', '2009', '109', '阳光男孩黄天阔（彭于晏 饰）的父母经营着一家便当店，某天，他在为听障游泳队送便当时邂逅了清纯美丽的姐妹花——小朋（陈妍希 饰）和秧秧（陈意涵 饰）。姐妹俩的父亲是一名传教士，常年在非洲工作。为了保障姐姐顺利参加听障奥运会，秧秧担负起赚取家用的重任。她每日来去匆匆，辛苦打着几份工。本身即掌握手语的天阔自然而然融入了姐妹俩的生活当中，为了接近心仪的秧秧，他时常在体育馆门口做生意，还费尽心思为秧秧制作爱心便当。相识时间虽短，却让两个年轻人的心越走越近。然而某次约会中，一个小小的误会阻断了他们的交往，与此同时，小朋也遭遇了一场灾难……', '2021-05-02 11:19:22', null);
INSERT INTO `tb_sys_film` VALUES ('25', '盗梦空间 Inception', '7', '/static/movies/images/dmkj.jpg', '克里斯托弗·诺兰', '克里斯托弗·诺兰', '美国', '艾利奥特·佩吉/汤姆·哈迪/渡边谦', '英语', '2010', '148', '道姆·柯布（莱昂纳多·迪卡普里奥 Leonardo DiCaprio 饰）与同事阿瑟（约瑟夫·戈登-莱维特 Joseph Gordon-Levitt 饰）和纳什（卢卡斯·哈斯 Lukas Haas 饰）在一次针对日本能源大亨齐藤（渡边谦 饰）的盗梦行动中失败，反被齐藤利用。齐藤威逼利诱因遭通缉而流亡海外的柯布帮他拆分他竞争对手的公司，采取极端措施在其唯一继承人罗伯特·费希尔（希里安·墨菲 Cillian Murphy 饰）的深层潜意识中种下放弃家族公司、自立门户的想法。为了重返美国，柯布偷偷求助于岳父迈尔斯（迈克尔·凯恩 Michael Caine 饰），吸收了年轻的梦境设计师艾里阿德妮（艾伦·佩吉 Ellen Page 饰）、梦境演员艾姆斯（汤姆·哈迪 Tom Hardy 饰）和药剂师约瑟夫（迪利普·劳 Dileep Rao 饰）加入行动。在一层层递进的梦境中，柯布不仅要对付费希尔潜意识的本能反抗，还必须直面已逝妻子梅尔（玛丽昂·歌迪亚 Marion Cotillard 饰）的处处破坏，实际情况远比预想危险得多……', '2021-05-02 11:19:23', null);
INSERT INTO `tb_sys_film` VALUES ('26', '流浪地球', '7', '/static/movies/images/lldq.jpg', '郭帆', '龚格尔', '中国', '屈楚萧/吴京/李光洁/吴孟达', '汉语普通话', '2019', '125', '近未来，科学家们发现太阳急速衰老膨胀，短时间内包括地球在内的整个太阳系都将被太阳所吞没。为了自救，人类提出一个名为“流浪地球”的大胆计划，即倾全球之力在地球表面建造上万座发动机和转向发动机，推动地球离开太阳系，用2500年的时间奔往另外一个栖息之地。中国航天员刘培强（吴京 饰）在儿子刘启四岁那年前往国际空间站，和国际同侪肩负起领航者的重任。转眼刘启（屈楚萧 饰）长大，他带着妹妹朵朵（赵今麦 饰）偷偷跑到地表，偷开外公韩子昂（吴孟达 饰）的运输车，结果不仅遭到逮捕，还遭遇了全球发动机停摆的事件。为了修好发动机，阻止地球坠入木星，全球开始展开饱和式营救，连刘启他们的车也被强征加入。在与时间赛跑的过程中，无数的人前仆后继，奋不顾身，只为延续百代子孙生存的希望……', '2021-05-02 11:19:23', null);
INSERT INTO `tb_sys_film` VALUES ('27', '楚门的世界', '7', '/static/movies/images/cmdsj.jpg', '彼得·威尔', '安德鲁·尼科尔', '美国', '金·凯瑞/劳拉·琳妮/艾德·哈里斯', '英语', '1998', '103', '楚门（金•凯瑞 Jim Carrey 饰）是一个平凡得不能再平凡的人，除了一些有些稀奇的经历之外——初恋女友突然失踪、溺水身亡的父亲忽然似乎又出现在眼前，他和绝大多数30多岁的美国男人绝无异样。这令他倍感失落。他也曾试过离开自己生活了多年的地方，但总因种种理由而不能成行。直到有一天，他忽然发觉自己似乎一直在被人跟踪，无论他走到哪里，干什么事情。这种感觉愈来愈强烈。楚门决定不惜一切代价逃离这个他生活了30多年的地方，去寻找他的初恋女友。但他却发现自己怎样也逃不出去。真相其实很残忍。', '2021-05-02 11:19:24', null);
INSERT INTO `tb_sys_film` VALUES ('28', '头号玩家', '7', '/static/movies/images/thwj.jpg', '史蒂文·斯皮尔伯格', '恩斯特·克莱恩', '美国', '泰伊·谢里丹/奥利维亚·库克', '英语', '2018', '140', '故事发生在2045年，虚拟现实技术已经渗透到了人类生活的每一个角落。詹姆斯哈利迪（马克·里朗斯 Mark Rylance 饰）一手建造了名为“绿洲”的虚拟现实游戏世界，临终前，他宣布自己在游戏中设置了一个彩蛋，找到这枚彩蛋的人即可成为绿洲的继承人。要找到这枚彩蛋，必须先获得三把钥匙，而寻找钥匙的线索就隐藏在詹姆斯的过往之中。韦德（泰尔·谢里丹 Tye Sheridan 饰）、艾奇（丽娜·维特 Lena Waithe 饰）、大东（森崎温 饰）和修（赵家正 饰）是游戏中的好友，和之后遇见的阿尔忒弥斯（奥利维亚·库克 Olivia Cooke 饰）一起，五人踏上了寻找彩蛋的征程。他们所要对抗的，是名为诺兰索伦托（本·门德尔森 Ben Mendelsohn 饰）的大资本家。', '2021-05-02 11:19:24', null);
INSERT INTO `tb_sys_film` VALUES ('29', '控方证人', '8', '/static/movies/images/kfzr.jpg', '比利·怀德', '比利·怀德', '美国', ' 泰隆·鲍华/玛琳·黛德丽', '英语', '1957', '116', '伦敦著名刑案辩护律师韦菲爵士（查尔斯•劳顿 Charles Laughton 饰）接受了心脏病治疗，但是身体依旧虚弱，第一天回家休养，护士一直严厉监督他服药，并杜绝烟酒。管家为了便于上楼，还专门为他修了电梯。但是，种种关心照顾，对于这位桀骜不驯、牙尖嘴利的大律师根本不起作用，反倒是一纸诉状令他倍感兴奋。律师梅休和当事人沃尔（泰隆•鲍华 Tyrone Power饰）登门拜访，请他出山打官司。原来，沃尔结识了富婆，两人相见甚欢，虽然仆人对他发明的打蛋器充满鄙夷，但是富婆却对他充满爱意，甚至为他修改了遗嘱，把8万英镑留给了他。然而，富婆却惨遭毒手。于是，沃尔成为警方的头号嫌疑犯。他的唯一证人是妻子克里斯汀（玛琳•黛德丽 Marlene Dietrich饰），然而后者登门时的冷漠与淡定，令韦菲爵士怀疑这其中另有隐情。在扑朔迷离的案件背后，隐藏着一个个环环相扣、不可告人的秘密……', '2021-05-02 11:19:26', null);
INSERT INTO `tb_sys_film` VALUES ('30', '看不见的客人', '8', '/static/movies/images/kbjdkr.jpg', '奥里奥尔·保罗', '奥里奥尔·保罗', '西班牙', '马里奥·卡萨斯/阿娜·瓦格纳', '西班牙语', '2017', '106', '艾德里安（马里奥·卡萨斯 Mario Casas 饰）经营着一间科技公司，事业蒸蒸日上，家中有美丽贤惠的妻子和活泼可爱的女儿，事业家庭双丰收的他是旁人羡慕的对象。然而，野心勃勃的艾德里安并未珍惜眼前来之不易的生活，一直以来，他和一位名叫劳拉（芭芭拉·蓝妮 Bárbara Lennie 饰）的女摄影师保持着肉体关系。某日幽会过后，两人驱车离开别墅，却在路上发生了车祸，为了掩盖事件的真相，两人决定将在车祸中死去的青年丹尼尔联同他的车一起沉入湖底。之后，劳拉遇见了一位善良的老人，老人将劳拉坏掉的车拉回家中修理，然而，令劳拉没有想到的是，这位老人，竟然就是丹尼尔的父亲。', '2021-05-02 11:19:26', null);
INSERT INTO `tb_sys_film` VALUES ('31', '杀人回忆', '8', '/static/movies/images/srhy.jpg', '奉俊昊', '奉俊昊', '韩国', '宋康昊/金相庆/金雷夏', '韩语', '2003', '123', '1986年，韩国京畿道华城郡，热得发昏的夏天，在田野边发现一具女尸，早已发臭。小镇警察朴探员（宋康昊饰）和汉城来的苏探员（金相庆饰）接手案件，唯一可证实的是这具女尸生前被强奸过。线索的严重缺乏让毫无经验的朴探员和搭档曹探员（金罗河饰）只凭粗暴逼供和第六感推断，几次将犯罪 嫌疑人屈打成招。而苏探员客观冷静，据理分析，几次排除嫌疑，警察内部为了证明与推翻矛盾不断，然而无辜女子还是接二连三被残忍杀害，他们只好达成共识一起合作。此时，一个极其符合作案特征的小青年（朴海日饰）成为最大嫌疑人，警方神经绷紧地锁定住他，同时DNA检测报告也被送往美国，然而案件并未在此处停止。', '2021-05-02 11:19:27', null);
INSERT INTO `tb_sys_film` VALUES ('32', '烈日灼心', '8', '/static/movies/images/lrzx.jpg', '曹保平', '曹保平', '中国', '邓超/段奕宏/郭涛/王珞丹', '汉语普通话', '2015', '139', '七年前，福建西陇发生一起惨绝人寰的灭门惨案，某别墅内一家五口同日惨死，在社会上引起极大的震动，然而此去经年，嫌疑人杨自道（郭涛 饰）、辛小丰（邓超 饰）、陈比觉（高虎 饰）却依然逍遥法外。现如今，这三个人都在厦门过活，杨当起了出租车司机，小丰加入了协警队伍，因意外变成傻子的陈则带着三人捡来的女孩尾巴栖息在亲戚的渔场中。这一天，拥有丰富办案经验的伊谷春（段奕宏 饰）调到小丰所在的队伍担任警长。伊颇为器重能力卓越的小丰，但嗅觉灵敏的他也隐隐觉出这个男子和当年的灭门惨案有所关联。在此期间，道哥在一次意外中结识了伊的妹妹小夏（王珞丹 饰）。仿佛冥冥之中有一只看不见的手，正将这三个决心重新做人的男人推向无法更改的结局……', '2021-05-02 11:19:27', null);
INSERT INTO `tb_sys_film` VALUES ('33', '闪灵', '9', '/static/movies/images/sl.jpg', '斯坦利·库布里克', '斯坦利·库布里克', '美国', '杰克·尼科尔森/谢莉·杜瓦尔', '英语', '1980', '119', '杰克（杰克•尼科尔森 Jack Nicholson 饰）是一个作家。一个冬天，他得到了一个看管山顶酒店的差事。这正合杰克的意思，他正好可以有一个幽静的地方写作。于是杰克带着妻儿搬进了酒店。冬天大雪封路，山顶酒店只有杰克一家三口。从他们一搬进来，杰克的妻子温蒂（谢莉·杜瓦尔 Shelley Duvall 饰）就发现这里气氛诡异，杰克的儿子丹尼（丹尼·劳埃德 Danny Lloyd 饰）经常看到一些他不认识的人，但这里除了他们一家别无他人。而杰克除了一整天闷头写作外，脾气亦变得越来越古怪。直到有一天，温蒂发现丈夫这些天来一直写就只有一句话：杰克发疯了！', '2021-05-02 11:19:30', null);
INSERT INTO `tb_sys_film` VALUES ('34', '汉江怪物', '9', '/static/movies/images/hjgw.jpg', '奉俊昊', '奉俊昊', '韩国', '宋康昊/边希峰/朴海日', '韩语', '2006', '120', '康斗（宋康昊饰）是一个平凡又有些糊涂的中年男人，他带着女儿玄舒（高雅成饰）与家人一起生活，在汉江边经营小店，过着安静祥和的生活。康斗父亲熙峰（边熙峰饰）是一个和蔼老人，弟弟南日（朴海日饰）是家里唯一的大学生，却失业没有工作。他时常牢骚并酗酒，对现实不满。妹妹南珠（裴斗娜饰）是个射箭运动员，却常在关键时刻掉链子。一天，人们聚集在汉江边上，惊奇地发现攀在大桥的桥墩上，一只从未见过的不明怪物。突然怪物窜到了岸边，冲进围观的人群中。康斗想马上带着女儿逃离，但玄舒却被怪物抓走了，这个普通家庭遭受了空前的打击，整个城市也如临大敌，他们该如何面对怪物？', '2021-05-02 11:19:30', null);
INSERT INTO `tb_sys_film` VALUES ('35', '贞子', '9', '/static/movies/images/zz.jpg', '中田秀夫', '杉原宪明', '日本', '池田依来沙/塚本高史', '日语', '2019', '99', '心理諮詢師茉優（池田依來沙 飾），負責看診一名由警察保護的少女。失去所有記憶的少女連自己的名字都忘了，而茉優身旁竟然也開始發生許多無法理解的怪事……另一方面，在網路行銷公司工作的祐介（塚本高史 飾）的推薦下，茉優的弟弟和真（清水尋也 飾）成為了一名YouTuber，和真為了增加影片的點閱量，打算偷偷到發生過5人死亡的火災地點拍攝靈異影片，沒想到這卻讓他……', '2021-05-02 11:19:31', null);
INSERT INTO `tb_sys_film` VALUES ('36', '笔仙', '9', '/static/movies/images/bx.jpg', '安兵基', '安兵基', '韩国', '李世恩/金圭莉', '韩语', '2004', '92', '从汉城转学到父母家乡的高中后，李玉甄（李世恩 饰）受到班里四个女生的欺负，为了报复，她和另外两个也常受她们欺负的同学玩起请笔仙的游戏，笔仙请来后，李玉甄施下诅咒，但也被鬼魂附身，不久，四个女生中有三人相继以自焚的方式死去，警察调查后，将目标人物锁定为李玉甄，可是此时李玉甄已 完全丧失记忆。新调来的美术老师李恩珠（金圭莉 饰）第一天上课点名时，喊到29号金仁淑的名字时，全班同学厉声尖叫，令她十分不解，在心仪她的同事的帮助下，她发现有关金仁淑的秘密，而这个秘密，也与李玉甄及死去的三名女生有很大干系。', '2021-05-02 11:19:32', null);
INSERT INTO `tb_sys_film` VALUES ('37', '忠犬八公的故事', '10', '/static/movies/images/zqbgdgs.jpg', '拉斯·霍尔斯道姆', '新藤兼人', '美国', '理查·基尔/萨拉·罗默尔', '英语', '2009', '93', '八公（Forest 饰）是一条谜一样的犬，因为没有人知道它从哪里来。教授帕克（理查·基尔 Richard Gere 饰）在小镇的火车站拣到一只走失的小狗，冥冥中似乎注定小狗和帕克教授有着某种缘分，帕克一抱起这只小狗就再也放不下来，最终，帕克对小狗八公的疼爱感化了起初极力反对养狗的妻子卡特（琼·艾伦 Joan Allen 饰）。八公在帕克的呵护下慢慢长大，帕克上班时八公会一直把他送到车站，下班时八公也会早早便爬在车站等候，八公的忠诚让小镇的人家对它更加疼爱。有一天，八公在帕克要上班时表现异常，居然玩起了以往从来不会的捡球游戏，八公的表现让帕克非常满意，可是就是在那天，帕克因病去世。帕克的妻子、女儿安迪（萨拉·罗默尔 Sarah Roemer 饰）及女婿迈克尔（罗比·萨布莱特 Robbie Sublett 饰）怀着无比沉痛的心情埋葬了帕克，可是不明就里的八公却依然每天傍晚五点准时守候在小站的门前，等待着主人归来……', '2021-05-02 11:19:34', null);
INSERT INTO `tb_sys_film` VALUES ('38', '寻梦环游记', '10', '/static/movies/images/xmhyj.jpg', '李·昂克里奇', '阿德里安·莫利纳', '美国', '安东尼·冈萨雷斯', '英语', '2017', '105', '热爱音乐的米格尔（安东尼·冈萨雷兹 Anthony Gonzalez 配音）不幸地出生在一个视音乐为洪水猛兽的大家庭之中，一家人只盼着米格尔快快长大，好继承家里传承了数代的制鞋产业。一年一度的亡灵节即将来临，每逢这一天，去世的亲人们的魂魄便可凭借着摆在祭坛上的照片返回现世和生者团圆。在一场意外中，米格尔竟然穿越到了亡灵国度之中，在太阳升起之前，他必须得到一位亲人的祝福，否则就将会永远地留在这个世界里。米格尔决定去寻找已故的歌神德拉库斯（本杰明·布拉特 Benjamin Bratt 配音），因为他很有可能就是自己的祖父。途中，米格尔邂逅了落魄乐手埃克托（盖尔·加西亚·贝纳尔 Gael García Bernal 配音），也渐渐发现了德拉库斯隐藏已久的秘密。', '2021-05-02 11:19:34', null);
INSERT INTO `tb_sys_film` VALUES ('39', '绿皮书', '10', '/static/movies/images/lps.jpg', '彼得·法雷里', '彼得·法雷里', '美国', '维果·莫腾森/马赫沙拉·阿里', '英语', '2018', '130', '托尼（维果·莫腾森 Viggo Mortensen 饰）是一个吊儿郎当游手好闲的混混，在一家夜总会做侍者。这间夜总会因故要停业几个月，可托尼所要支付的房租和生活费不会因此取消，所以他的当务之急是去寻找另一份工作来填补这几个月的空缺。在这个节骨眼上，一位名叫唐雪莉（马赫沙拉·阿里 Mahershala Ali 饰）的黑人钢琴家提出雇佣托尼。唐雪莉即将开始为期八个星期的南下巡回演出，可是，那个时候南方对黑人的歧视非常的严重，于是托尼便成为了唐雪莉的司机兼保镖。一路上，两人迥异的性格使得他们之间产生了很多的矛盾，与此同时，唐雪莉在南方所遭受的种种不公平的对待也让托尼对种族歧视感到深恶痛绝。', '2021-05-02 11:19:35', null);
INSERT INTO `tb_sys_film` VALUES ('40', '触不可及', '10', '/static/movies/images/cbkj.jpg', '奥利维埃·纳卡什 ', '奥利维埃·纳卡什 ', '法国', '弗朗索瓦·克鲁塞/奥玛·希', '法语', '2011', '112', '因为一次跳伞事故，白人富翁菲利普Philippe（弗朗索瓦·克鲁塞 François Cluzet 饰）瘫痪在床，欲招聘一名全职陪护。由于薪酬高，应聘者云集，个个舌灿莲花，却无法打动他的心。直到黑人德希斯Driss（奥玛·赛 Omar Sy 饰）的出现才让他作出决定。德希斯刚从监狱出来，背负家庭重担，一心只想寻张辞退信以申领救济金，而且他明显对女助理的兴趣要远大于这份工作。但是菲利普还是一眼相中了这个小伙子。于是，德希斯开始了一个月的试用期。虽然舒适的豪宅环境让他倍感虚荣，但是他仍面临很多挑战：不仅要为菲利普作身体复健，还得给他洗浴、灌肠、拆信、穿丝袜等。起初，两人的思维方式与价值观大相径庭，但是，随着了解的不断深入，他们成为了朋友……本片取材于真实事件，获2011年东京电影节最佳影片金麒麟奖，弗朗索瓦·克鲁塞与奥玛·赛分享影帝桂冠。', '2021-05-02 11:19:36', null);
INSERT INTO `tb_sys_film` VALUES ('86', '釜山行', '3', '/static/movies/images/fsx.jpg', '延尚昊', '延尚昊', '韩国', '孔刘/郑有美/马东锡', '韩语', '2016', '118', '证券公司基金管理人石宇（孔侑 饰）光鲜精干，却也是个重利轻义之徒。妻子为此与之决裂，女儿秀安（金秀安 饰）则对如此自私的父亲越来越失望，决定前往釜山和母亲生活。在秀安生日这天，石宇抽出时间陪伴女儿登上开往釜山的特快列车。而与此同时，城市四处出现了极为可疑的暴动事件。政府极力洗白无法掩盖丧尸肆虐的事实，即便懵然无知的列车乘客也因为不速之客的到来而堕入恐慌绝望的地狱中。开车的刹那，一名感染者冲入车厢，而她很快尸变并对目光所及之处的健康人展开血腥屠杀。未过多久，丧尸便呈几何数爆发性地增长。石宇被迫和幸存者的乘客们在逼仄的空间中奋力求生。', '2021-05-13 16:41:03', null);
INSERT INTO `tb_sys_film` VALUES ('116', '我不是药神', '5', '/static/movies/images/wbsys.jpg', '文牧野', '韩家女', '中国', '徐峥/王传君/周一围', '汉语普通话', '2018', '117', '普通中年男子程勇（徐峥 饰）经营着一家保健品店，失意又失婚。不速之客吕受益（王传君 饰）的到来，让他开辟了一条去印度买药做“代购”的新事业，虽然困难重重，但他在这条“买药之路”上发现了商机，一发不可收拾地做起了治疗慢粒白血病的印度仿制药独家代理商。赚钱的同时，他也认识了几个病患及家属，为救女儿被迫做舞女的思慧（谭卓 饰）、说一口流利“神父腔”英语的刘牧师（杨新鸣 饰），以及脾气暴烈的“黄毛”（章宇 饰），几个人合伙做起了生意，利润倍增的同时也危机四伏。程勇昔日的小舅子曹警官（周一围 饰）奉命调查仿制药的源头，假药贩子张长林（王砚辉 饰）和瑞士正牌医药代表（李乃文 饰）也对其虎视眈眈，生意逐渐变成了一场关于救赎的拉锯战。', '2021-05-14 15:02:30', null);
INSERT INTO `tb_sys_film` VALUES ('117', '测试删除', '2', '/static/movies/images/delete.png', '测试', '测试', '测试', '测试', '', '2000', '116', '测试', '2021-05-30 08:47:36', null);

-- ----------------------------
-- Table structure for tb_sys_film_category
-- ----------------------------
DROP TABLE IF EXISTS `tb_sys_film_category`;
CREATE TABLE `tb_sys_film_category` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '电影类型名称',
  `code` varchar(255) NOT NULL COMMENT '类型编码',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COMMENT='电影类型表';

-- ----------------------------
-- Records of tb_sys_film_category
-- ----------------------------
INSERT INTO `tb_sys_film_category` VALUES ('1', '华语', '001', '2021-05-02 10:42:54', null);
INSERT INTO `tb_sys_film_category` VALUES ('2', '欧美', '002', '2021-05-02 10:44:57', '2021-05-02 12:45:18');
INSERT INTO `tb_sys_film_category` VALUES ('3', '韩国', '003', '2021-05-02 10:58:31', '2021-05-02 12:46:15');
INSERT INTO `tb_sys_film_category` VALUES ('4', '日本', '004', '2021-05-02 10:46:08', null);
INSERT INTO `tb_sys_film_category` VALUES ('5', '喜剧', '005', '2021-05-02 10:46:38', null);
INSERT INTO `tb_sys_film_category` VALUES ('6', '爱情', '006', '2021-05-02 10:46:54', null);
INSERT INTO `tb_sys_film_category` VALUES ('7', '科幻', '007', '2021-05-02 10:47:26', null);
INSERT INTO `tb_sys_film_category` VALUES ('8', '悬疑', '008', '2021-05-02 10:47:47', null);
INSERT INTO `tb_sys_film_category` VALUES ('9', '恐怖', '009', '2021-05-02 10:48:03', null);
INSERT INTO `tb_sys_film_category` VALUES ('10', '治愈', '010', '2021-05-02 10:57:43', null);
INSERT INTO `tb_sys_film_category` VALUES ('11', '音乐', '011', '2021-05-25 11:08:38', '2021-05-25 14:09:07');

-- ----------------------------
-- Table structure for tb_sys_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_sys_user`;
CREATE TABLE `tb_sys_user` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(255) NOT NULL COMMENT '名字',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `email` varchar(255) NOT NULL COMMENT '邮箱',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `role` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ----------------------------
-- Records of tb_sys_user
-- ----------------------------
INSERT INTO `tb_sys_user` VALUES ('1', 'doris', '12345678', '1187773627@qq.com', '2021-05-01 18:15:52', null, 'user');
INSERT INTO `tb_sys_user` VALUES ('2', 'mro', '12345678', 'marui@163.com', '2021-05-01 18:16:13', null, 'user');
INSERT INTO `tb_sys_user` VALUES ('3', 'rjs', '12345678', 'rjs@163.com', '2021-05-01 18:16:26', null, 'user');
INSERT INTO `tb_sys_user` VALUES ('4', 'mr', '123456', 'maruioo@163.com', '2021-05-01 18:43:07', null, 'admin');
INSERT INTO `tb_sys_user` VALUES ('5', 'maruio', '12345678', '1125@163.com', '2021-05-01 18:54:02', null, 'user');
INSERT INTO `tb_sys_user` VALUES ('6', '番茄', '12345678', '111@163.com', '2021-05-06 15:45:12', null, 'user');
INSERT INTO `tb_sys_user` VALUES ('7', 'mmmmmarui', '12345678', 'mr@163.com', '2021-05-01 20:13:58', null, 'user');
INSERT INTO `tb_sys_user` VALUES ('8', 'mmmm', '12345678', 'mr@163.com', '2021-05-02 10:17:02', null, 'user');
INSERT INTO `tb_sys_user` VALUES ('9', 'zsl', '12345678', '1250884316@qq.com', '2021-05-14 14:47:17', null, 'user');
INSERT INTO `tb_sys_user` VALUES ('10', '风引o', '12345678', 'mr@163.com', '2021-05-30 10:58:14', null, 'user');

-- ----------------------------
-- Table structure for tb_sys_user_scan
-- ----------------------------
DROP TABLE IF EXISTS `tb_sys_user_scan`;
CREATE TABLE `tb_sys_user_scan` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `film_id` bigint(11) DEFAULT NULL COMMENT '电影ID',
  `user_id` bigint(50) DEFAULT NULL COMMENT '用户ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8 COMMENT='用户浏览记录表-每看一个详情就记录进去';

-- ----------------------------
-- Records of tb_sys_user_scan
-- ----------------------------
INSERT INTO `tb_sys_user_scan` VALUES ('1', '1', '1', '2021-05-13 14:54:26', null);
INSERT INTO `tb_sys_user_scan` VALUES ('2', '3', '2', '2021-05-13 15:08:24', null);
INSERT INTO `tb_sys_user_scan` VALUES ('3', '3', '2', '2021-05-18 16:38:02', null);
INSERT INTO `tb_sys_user_scan` VALUES ('4', '3', '2', '2021-05-18 16:38:47', null);
INSERT INTO `tb_sys_user_scan` VALUES ('5', '22', '2', '2021-05-18 16:41:59', null);
INSERT INTO `tb_sys_user_scan` VALUES ('8', '3', '2', '2021-05-27 10:30:37', null);
INSERT INTO `tb_sys_user_scan` VALUES ('9', '3', '2', '2021-05-27 10:30:48', null);
INSERT INTO `tb_sys_user_scan` VALUES ('10', '3', '2', '2021-05-29 09:36:45', null);
INSERT INTO `tb_sys_user_scan` VALUES ('11', '3', '2', '2021-05-29 09:39:49', null);
INSERT INTO `tb_sys_user_scan` VALUES ('12', '3', '2', '2021-05-29 09:42:10', null);
INSERT INTO `tb_sys_user_scan` VALUES ('13', '3', '2', '2021-05-29 09:43:56', null);
INSERT INTO `tb_sys_user_scan` VALUES ('14', '3', '2', '2021-05-29 09:44:04', null);
INSERT INTO `tb_sys_user_scan` VALUES ('15', '3', '2', '2021-05-29 09:45:29', null);
INSERT INTO `tb_sys_user_scan` VALUES ('16', '3', '2', '2021-05-29 09:45:36', null);
INSERT INTO `tb_sys_user_scan` VALUES ('17', '3', '2', '2021-05-29 09:47:00', null);
INSERT INTO `tb_sys_user_scan` VALUES ('18', '22', '2', '2021-05-29 09:47:10', null);
INSERT INTO `tb_sys_user_scan` VALUES ('19', '14', '2', '2021-05-29 09:47:15', null);
INSERT INTO `tb_sys_user_scan` VALUES ('20', '14', '2', '2021-05-29 09:51:14', null);
INSERT INTO `tb_sys_user_scan` VALUES ('21', '14', '2', '2021-05-29 09:51:20', null);
INSERT INTO `tb_sys_user_scan` VALUES ('22', '3', '2', '2021-05-29 11:38:19', null);
INSERT INTO `tb_sys_user_scan` VALUES ('23', '3', '2', '2021-05-29 21:31:42', null);
INSERT INTO `tb_sys_user_scan` VALUES ('24', '3', '2', '2021-05-29 21:31:47', null);
INSERT INTO `tb_sys_user_scan` VALUES ('25', '8', '2', '2021-05-29 21:35:34', null);
INSERT INTO `tb_sys_user_scan` VALUES ('26', '8', '2', '2021-05-29 21:35:41', null);
INSERT INTO `tb_sys_user_scan` VALUES ('27', '11', '2', '2021-05-29 21:35:47', null);
INSERT INTO `tb_sys_user_scan` VALUES ('28', '11', '2', '2021-05-29 21:35:53', null);
INSERT INTO `tb_sys_user_scan` VALUES ('29', '7', '2', '2021-05-29 21:53:56', null);
INSERT INTO `tb_sys_user_scan` VALUES ('30', '7', '2', '2021-05-29 21:54:00', null);
INSERT INTO `tb_sys_user_scan` VALUES ('31', '12', '2', '2021-05-29 21:54:05', null);
INSERT INTO `tb_sys_user_scan` VALUES ('32', '12', '2', '2021-05-29 21:54:09', null);
INSERT INTO `tb_sys_user_scan` VALUES ('33', '3', '2', '2021-05-30 01:30:41', null);
INSERT INTO `tb_sys_user_scan` VALUES ('34', '4', '2', '2021-05-30 01:31:28', null);
INSERT INTO `tb_sys_user_scan` VALUES ('35', '4', '3', '2021-05-30 01:32:01', null);
INSERT INTO `tb_sys_user_scan` VALUES ('36', '4', '3', '2021-05-30 01:32:06', null);
INSERT INTO `tb_sys_user_scan` VALUES ('37', '3', '2', '2021-05-30 13:21:26', null);
INSERT INTO `tb_sys_user_scan` VALUES ('38', '3', '2', '2021-05-30 14:18:39', null);

-- ----------------------------
-- Table structure for tb_sys_user_score
-- ----------------------------
DROP TABLE IF EXISTS `tb_sys_user_score`;
CREATE TABLE `tb_sys_user_score` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `film_id` bigint(11) NOT NULL COMMENT '电影ID',
  `user_id` bigint(11) NOT NULL COMMENT '用户ID',
  `score` int(10) NOT NULL COMMENT '用户评分',
  `remark` varchar(255) NOT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COMMENT='用户评分表';

-- ----------------------------
-- Records of tb_sys_user_score
-- ----------------------------
INSERT INTO `tb_sys_user_score` VALUES ('1', '2', '1', '4', '总的来说，整体剧本没太大问题，推理不算精彩但能看，笑点不出众但还及格，最大的问题是塞得太满但处理的又不够好，不懂做减法，每隔一段时间就会出现一些尴尬的戏份，割裂感太强，导致观影不够流畅，如果删减修改十分钟效果会好不少。', '2021-05-06 15:40:39', null);
INSERT INTO `tb_sys_user_score` VALUES ('2', '3', '2', '5', '这部电影真的太有心了，身在绝处的时候突然在眼前出现了一道光，小北和陈念是彼此生命中的那道光，小北不想让陈念在泥泞里，努力把她推向美好的未来的这个设定真的太戳我了，救赎向真的很感动。而两位主演也诠释的很好，所有情绪都很到位，周冬雨一如既往的好，四字弟弟是第一次登上大荧幕，也很让我敬佩，导演真的很有心，谢谢有这么好的电影出现，祝票房大卖。', '2021-05-06 15:41:06', null);
INSERT INTO `tb_sys_user_score` VALUES ('3', '3', '1', '5', '这个题材，做成这样，真的很不容易了。尤其是，影片在影像上非常有特点，大量的特写镜头、跟拍、手持，以及碎片式剪辑，营造出强烈的恐慌与不安感。这种情绪，其实就是角色在片中的情绪，它以影像的方式，最直接延伸给观众。', '2021-05-06 15:45:32', null);
INSERT INTO `tb_sys_user_score` VALUES ('4', '3', '6', '4', '没有人会孤独，我们都是社会的一部分。 没有人有权利绝望，我们要使明天更好。', '2021-05-06 15:46:21', null);
INSERT INTO `tb_sys_user_score` VALUES ('5', '1', '6', '3', '讽刺的是，尽管角色口口声声说着“做自己”、“我命由己不由天”、“最害人的是成见”，电影却依然一直在用对肥胖、结巴、娘娘腔等特征产生的成见制造无价值，且一点都不好笑的笑料。', '2021-05-06 15:47:19', null);
INSERT INTO `tb_sys_user_score` VALUES ('6', '1', '2', '5', '牛逼了！没想到国产动画能把故事内核写得这么成熟，远超《大圣归来》。好几场戏被震撼到，必须五星鼓励了。将“水淹陈塘关”的故事元素打碎了重构，人设和剧情完全依照阴阳哲学来做。太极生两仪，混元珠分为魔丸（火）和灵珠（水）。', '2021-05-06 15:47:36', null);
INSERT INTO `tb_sys_user_score` VALUES ('8', '22', '1', '5', '两代人的相似经历，是缘分，是天意。永远记得赵寅成撑开衣服为孙艺珍遮雨，两人一起奔跑的镜头，唯美浪漫，怦然心动。', '2021-05-06 15:49:10', null);
INSERT INTO `tb_sys_user_score` VALUES ('9', '26', '6', '4', '《流浪地球》小说本就想象力丰富，让地球连同地球人一同逃难的创意荡气回肠、惊艳无比。 电影中的行星发动机很硬核，还原了小说中“雅典卫城神殿的巨柱”的描写； 地球启航产生的尾迹清晰可见，宛如一艘巨型飞船遨游宇宙之间； 北京、上海被冰雪覆盖气势磅礴；地木交会更是让人瞠目结舌，试问有电影能看到这样一幕？ 最后表扬一下片尾的《流浪地球》小说的特效，比漫威的漫画翻页片头还精彩。', '2021-05-06 15:49:57', null);
INSERT INTO `tb_sys_user_score` VALUES ('10', '4', '2', '4', '一幅徐徐展开的自画像，无须加诸过多的戏剧注解，更与励志鸡汤无关。易烊千玺再次证明了他对于表演的超群领悟力，他几乎走向“演”的反面，不追求爆发，而是小心翼翼地隐藏，用紧绷的身体和躲闪的眼神靠近一种充满敌意的留恋，那里既有情窦初开的清澈，又有千帆过尽的沧桑。有那么多闪烁其词和言不由衷的时刻，唯独表白是坚定的，因为那一刻，他不再惧怕袒露自我，也不再纠结于得到还是失去。', '2021-05-06 15:51:06', null);
INSERT INTO `tb_sys_user_score` VALUES ('11', '6', '6', '5', '时间可以伸缩和折叠，唯独不能倒退。你的鹤发或许是我的童颜，而我一次呼吸能抵过你此生的岁月。', '2021-05-06 15:51:50', null);
INSERT INTO `tb_sys_user_score` VALUES ('12', '21', '3', '5', '每当我对世局倍感忧虑时，就会想到希思罗机场的入境闸口。人们认为世界充满仇恨与贪婪，但我却不同意。在我看来爱无处不在，虽然未必来的轰轰烈烈，但是爱永远存在。父子，母女，夫妻，男朋友，女朋友，老朋友，当飞机撞上世贸大厦时，临终打出的电话，谈的不是报复，而都是爱。如果你肯留意，你会发现爱其实无处不在。love actually is all around.', '2021-05-08 21:00:01', null);
INSERT INTO `tb_sys_user_score` VALUES ('13', '22', '2', '4', '雨中那一幕真的很好看~', '2021-05-09 11:24:13', null);
INSERT INTO `tb_sys_user_score` VALUES ('17', '14', '2', '4', '没有看过原漫 所以剧情不予置评。单从假期窝在沙发上挑一部电影来看的角度，我觉得还不错。主角都很养眼 电影画面很美 BGM也好听应景 是he的纯爱片。唯一的槽点就是由奈表白的时候 我以为她要晕倒 lol', '2021-05-29 09:51:18', null);
INSERT INTO `tb_sys_user_score` VALUES ('20', '7', '2', '5', '1111', '2021-05-29 21:53:59', null);
INSERT INTO `tb_sys_user_score` VALUES ('22', '4', '3', '5', '疾病并不可怕，可怕的是人有战胜它的心。承袭韩延导演《滚蛋吧！肿瘤君！》又一最新力作，香港金像奖最佳新人四字弟弟领衔主演。一朵小红花，串联起无数个与病魔抗争的个人和家庭。死亡面前，我们能做的，是保持希望和珍惜，用爱发光，战胜病魔。温情而现实，绝望又充满希望，是对2020最好的总结，也是对2021最好的期许。', '2021-05-30 01:32:05', null);
