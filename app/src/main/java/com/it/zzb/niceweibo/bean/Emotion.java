package com.it.zzb.niceweibo.bean;

import com.it.zzb.niceweibo.R;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
public class Emotion implements Serializable {
	
	public static Map<String, Integer> emojiMap;
	
	static {
		emojiMap = new HashMap<String, Integer>();
		emojiMap.put("[呵呵]", R.mipmap.d_hehe);
		emojiMap.put("[嘻嘻]", R.mipmap.d_xixi);
		emojiMap.put("[哈哈]", R.mipmap.d_haha);
		emojiMap.put("[爱你]", R.mipmap.d_aini);
		emojiMap.put("[挖鼻屎]", R.mipmap.d_wabishi);
		emojiMap.put("[吃惊]", R.mipmap.d_chijing);
		emojiMap.put("[晕]", R.mipmap.d_yun);
		emojiMap.put("[泪]", R.mipmap.d_lei);
		emojiMap.put("[馋嘴]", R.mipmap.d_chanzui);
		emojiMap.put("[抓狂]", R.mipmap.d_zhuakuang);
		emojiMap.put("[哼]", R.mipmap.d_heng);
		emojiMap.put("[可爱]", R.mipmap.d_keai);
		emojiMap.put("[怒]", R.mipmap.d_nu);
		emojiMap.put("[汗]", R.mipmap.d_han);
		emojiMap.put("[害羞]", R.mipmap.d_haixiu);
		emojiMap.put("[睡觉]", R.mipmap.d_shuijiao);
		emojiMap.put("[钱]", R.mipmap.d_qian);
		emojiMap.put("[偷笑]", R.mipmap.d_touxiao);
		emojiMap.put("[笑cry]", R.mipmap.d_xiaoku);
		emojiMap.put("[doge]", R.mipmap.d_doge);
		emojiMap.put("[喵喵]", R.mipmap.d_miao);
		emojiMap.put("[酷]", R.mipmap.d_ku);
		emojiMap.put("[衰]", R.mipmap.d_shuai);
		emojiMap.put("[闭嘴]", R.mipmap.d_bizui);
		emojiMap.put("[鄙视]", R.mipmap.d_bishi);
		emojiMap.put("[花心]", R.mipmap.d_huaxin);
		emojiMap.put("[鼓掌]", R.mipmap.d_guzhang);
		emojiMap.put("[悲伤]", R.mipmap.d_beishang);
		emojiMap.put("[思考]", R.mipmap.d_sikao);
		emojiMap.put("[生病]", R.mipmap.d_shengbing);
		emojiMap.put("[亲亲]", R.mipmap.d_qinqin);
		emojiMap.put("[怒骂]", R.mipmap.d_numa);
		emojiMap.put("[太开心]", R.mipmap.d_taikaixin);
		emojiMap.put("[懒得理你]", R.mipmap.d_landelini);
		emojiMap.put("[右哼哼]", R.mipmap.d_youhengheng);
		emojiMap.put("[左哼哼]", R.mipmap.d_zuohengheng);
		emojiMap.put("[嘘]", R.mipmap.d_xu);
		emojiMap.put("[委屈]", R.mipmap.d_weiqu);
		emojiMap.put("[吐]", R.mipmap.d_tu);
		emojiMap.put("[可怜]", R.mipmap.d_kelian);
		emojiMap.put("[打哈气]", R.mipmap.d_dahaqi);
		emojiMap.put("[挤眼]", R.mipmap.d_jiyan);
		emojiMap.put("[失望]", R.mipmap.d_shiwang);
		emojiMap.put("[顶]", R.mipmap.d_ding);
		emojiMap.put("[疑问]", R.mipmap.d_yiwen);
		emojiMap.put("[困]", R.mipmap.d_kun);
		emojiMap.put("[感冒]", R.mipmap.d_ganmao);
		emojiMap.put("[拜拜]", R.mipmap.d_baibai);
		emojiMap.put("[黑线]", R.mipmap.d_heixian);
		emojiMap.put("[阴险]", R.mipmap.d_yinxian);
		emojiMap.put("[打脸]", R.mipmap.d_dalian);
		emojiMap.put("[傻眼]", R.mipmap.d_shayan);
		emojiMap.put("[猪头]", R.mipmap.d_zhutou);
		emojiMap.put("[熊猫]", R.mipmap.d_xiongmao);
		emojiMap.put("[兔子]", R.mipmap.d_tuzi);

		//浪小花表情
		emojiMap.put("[悲催]", R.mipmap.lxh_beicui);
		emojiMap.put("[被电]", R.mipmap.lxh_beidian);
		emojiMap.put("[崩溃]", R.mipmap.lxh_bengkui);
		emojiMap.put("[别烦我]", R.mipmap.lxh_biefanwo);
		emojiMap.put("[不好意思]", R.mipmap.lxh_buhaoyisi);
		emojiMap.put("[不想上班]", R.mipmap.lxh_buxiangshangban);
		emojiMap.put("[得意地笑]", R.mipmap.lxh_deyidexiao);
		emojiMap.put("[给劲]", R.mipmap.lxh_feijin);
		emojiMap.put("[好爱哦]", R.mipmap.lxh_haoaio);
		emojiMap.put("[好棒]", R.mipmap.lxh_haobang);
		emojiMap.put("[好囧]", R.mipmap.lxh_haojiong);
		emojiMap.put("[好喜欢]", R.mipmap.lxh_haoxihuan);
		emojiMap.put("[hold住]", R.mipmap.lxh_holdzhu);
		emojiMap.put("[杰克逊]", R.mipmap.lxh_jiekexun);
		emojiMap.put("[纠结]", R.mipmap.lxh_jiujie);
		emojiMap.put("[巨汗]", R.mipmap.lxh_juhan);
		emojiMap.put("[抠鼻屎]", R.mipmap.lxh_koubishi);
		emojiMap.put("[困死了]", R.mipmap.lxh_kunsile);
		emojiMap.put("[雷锋]", R.mipmap.lxh_leifeng);
		emojiMap.put("[泪流满面]", R.mipmap.lxh_leiliumanmian);
		emojiMap.put("[玫瑰]", R.mipmap.lxh_meigui);
		emojiMap.put("[噢耶]", R.mipmap.lxh_oye);
		emojiMap.put("[霹雳]", R.mipmap.lxh_pili);
		emojiMap.put("[瞧瞧]", R.mipmap.lxh_qiaoqiao);
		emojiMap.put("[丘比特]", R.mipmap.lxh_qiubite);
		emojiMap.put("[求关注]", R.mipmap.lxh_qiuguanzhu);
		emojiMap.put("[群体围观]", R.mipmap.lxh_quntiweiguan);
		emojiMap.put("[甩甩手]", R.mipmap.lxh_shuaishuaishou);
		emojiMap.put("[偷乐]", R.mipmap.lxh_toule);
		emojiMap.put("[推荐]", R.mipmap.lxh_tuijian);
		emojiMap.put("[互相膜拜]", R.mipmap.lxh_xianghumobai);
		emojiMap.put("[想一想]", R.mipmap.lxh_xiangyixiang);
		emojiMap.put("[笑哈哈]", R.mipmap.lxh_xiaohaha);
		emojiMap.put("[羞嗒嗒]", R.mipmap.lxh_xiudada);
		emojiMap.put("[许愿]", R.mipmap.lxh_xuyuan);
		emojiMap.put("[有鸭梨]", R.mipmap.lxh_youyali);
		emojiMap.put("[赞啊]", R.mipmap.lxh_zana);
		emojiMap.put("[震惊]", R.mipmap.lxh_zhenjing);
		emojiMap.put("[转发]", R.mipmap.lxh_zhuanfa);

		//其他
		emojiMap.put("[蛋糕]", R.mipmap.o_dangao);
		emojiMap.put("[飞机]", R.mipmap.o_feiji);
		emojiMap.put("[干杯]", R.mipmap.o_ganbei);
		emojiMap.put("[话筒]", R.mipmap.o_huatong);
		emojiMap.put("[蜡烛]", R.mipmap.o_lazhu);
		emojiMap.put("[礼物]", R.mipmap.o_liwu);
		emojiMap.put("[围观]", R.mipmap.o_weiguan);
		emojiMap.put("[咖啡]", R.mipmap.o_kafei);
		emojiMap.put("[足球]", R.mipmap.o_zuqiu);

		emojiMap.put("[ok]", R.mipmap.h_ok);
		emojiMap.put("[躁狂症]", R.mipmap.lxh_zaokuangzheng);
		emojiMap.put("[威武]", R.mipmap.weiwu);
		emojiMap.put("[赞]", R.mipmap.h_zan);
		emojiMap.put("[心]", R.mipmap.l_xin);
		emojiMap.put("[伤心]", R.mipmap.l_shangxin);
		emojiMap.put("[月亮]", R.mipmap.w_yueliang);
		emojiMap.put("[鲜花]", R.mipmap.w_xianhua);
		emojiMap.put("[太阳]", R.mipmap.w_taiyang);
		emojiMap.put("[威武]", R.mipmap.weiwu);
		emojiMap.put("[浮云]", R.mipmap.w_fuyun);
		emojiMap.put("[神马]", R.mipmap.shenma);
		emojiMap.put("[微风]", R.mipmap.w_weifeng);
		emojiMap.put("[下雨]", R.mipmap.w_xiayu);

		emojiMap.put("[沙尘暴]", R.mipmap.w_shachenbao);
		emojiMap.put("[落叶]", R.mipmap.w_luoye);
		emojiMap.put("[雪人]", R.mipmap.w_xueren);
		emojiMap.put("[good]", R.mipmap.h_good);
		emojiMap.put("[哆啦A梦吃惊]", R.mipmap.dorahaose_mobile);
		emojiMap.put("[哆啦A梦微笑]", R.mipmap.jqmweixiao_mobile);
		emojiMap.put("[哆啦A梦花心]", R.mipmap.dorahaose_mobile);
		emojiMap.put("[弱]", R.mipmap.ruo);
		emojiMap.put("[炸鸡啤酒]", R.mipmap.d_zhajipijiu);
		emojiMap.put("[囧]", R.mipmap.jiong);
		emojiMap.put("[NO]", R.mipmap.buyao);
		emojiMap.put("[来]", R.mipmap.guolai);
		emojiMap.put("[互粉]", R.mipmap.f_hufen);
		emojiMap.put("[握手]", R.mipmap.h_woshou);
		emojiMap.put("[haha]", R.mipmap.h_haha);
		emojiMap.put("[织]", R.mipmap.zhi);
		emojiMap.put("[萌]", R.mipmap.meng);
		emojiMap.put("[钟]", R.mipmap.o_zhong);
		emojiMap.put("[给力]", R.mipmap.geili);
		emojiMap.put("[喜]", R.mipmap.xi);
		emojiMap.put("[绿丝带]", R.mipmap.o_lvsidai);
		emojiMap.put("[围脖]", R.mipmap.weibo);
		emojiMap.put("[音乐]", R.mipmap.o_yinyue);
		emojiMap.put("[照相机]", R.mipmap.o_zhaoxiangji);
		emojiMap.put("[耶]", R.mipmap.h_ye);
		emojiMap.put("[拍照]", R.mipmap.lxhpz_paizhao);
		emojiMap.put("[白眼]", R.mipmap.landeln_baiyan);


		emojiMap.put("[作揖]", R.mipmap.o_zuoyi);
		emojiMap.put("[拳头]", R.mipmap.quantou_org);
		emojiMap.put("[X教授]", R.mipmap.xman_jiaoshou);
		emojiMap.put("[天启]", R.mipmap.xman_tianqi);
		emojiMap.put("[抢到啦]", R.mipmap.hb_qiangdao_org);
	}
	
	public static int getImgByName(String imgName) {
		Integer integer = emojiMap.get(imgName);
		return integer == null ? -1 : integer;
	}
}
