<p align="center">
    为中国审批流程开发
</p>

<p align="center">
  <a href="https://www.apache.org/licenses/LICENSE-2.0">
    <img alt="code style" src="https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?style=flat-square">
  </a>
</p>

# 简介 | Intro

对接国内多个热门OA系统，解决公司切换OA系统困扰，审批流对接的烦恼。

目前只实现了发起审批流、接收审批流回调基础功能。持续开发中，敬请期待。。。

# 优点 | Advantages

- **统一接口封装**：解决多个平台开放接口不同，需要对接多个平台的接口。实现将多个平台统一封装，一套代码，对接多个第三方。
- **依赖少**：仅仅依赖对应平台
- **统一回调处理**：实现多个第三方回调，整理成统一格式，方便统一处理回调事件。

# 代码实例-发起审批如此简单
```java
        InstanceStart instanceStart = InstanceStart.builder().processCode(larkTestProcessCode)
                .userId("123456789")
                .deptId("123456789")
                .targetSelectUsersAuthMatch(
                        Arrays.asList(InstanceStart.TargetSelectUserAuthMatch.builder()
                                        .userIds(Arrays.asList("1"))
                                        .build()
                                , InstanceStart.TargetSelectUserAuthMatch.builder()
                                        .userIds(Arrays.asList("1"))
                                        .build()
                        ))
                .formData(
                        Arrays.asList(
                                FormDataItem.text("单行文本", "测试"),
                                FormDataItem.textarea("多行文本", "测试"),
                                FormDataItem.number("数字", 1),
                                FormDataItem.amount("金额", new BigDecimal("1.22"), AmountOption.AmountType.CNY),
                                FormDataItem.select("单选", "选项 1"),
                                FormDataItem.multiSelect("多选", "选项 1", "选项 2"),
                                FormDataItem.date("日期", LocalDateTime.now(), DateOption.ComponentDateFormat.YYYY_MM_DD),
                                FormDataItem.dateRange("开始时间", LocalDateTime.now(), "结束时间", LocalDateTime.now(), DateOption.ComponentDateFormat.YYYY_MM_DD_HH_MM),
                                FormDataItem.image("图片", Arrays.asList(
                                        FormDataImage.builder().name("测试1.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build(),
                                        FormDataImage.builder().name("测试2.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build(),
                                        FormDataImage.builder().name("测试3.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build())),
                                FormDataItem.attachment("附件", Arrays.asList(
                                        FormDataAttachment.builder().name("测试1.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build(),
                                        FormDataAttachment.builder().name("测试2.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build(),
                                        FormDataAttachment.builder().name("测试3.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build())),
                                FormDataItem.table("表格",
                                        Arrays.asList(
                                                Arrays.asList(
                                                        FormDataItem.text("单行文本", "表格单行输入框1"),
                                                        FormDataItem.date("日期", LocalDateTime.now(), DateOption.ComponentDateFormat.YYYY_MM_DD_HH_MM),
                                                        FormDataItem.attachment("附件", Arrays.asList(
                                                                FormDataAttachment.builder().name("测试1.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build(),
                                                                FormDataAttachment.builder().name("测试2.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build()))
                                                ),
                                                Arrays.asList(
                                                        FormDataItem.text("单行文本", "表格单行输入框2"),
                                                        FormDataItem.date("日期", LocalDateTime.now(), DateOption.ComponentDateFormat.YYYY_MM_DD_HH_MM),
                                                        FormDataItem.attachment("附件", Arrays.asList(
                                                                FormDataAttachment.builder().name("测试1.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build(),
                                                                FormDataAttachment.builder().name("测试2.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build()))
                                                )))))
                .build();

        Factory.getInstancesService("dingtalk")
                .start(instanceStart);
```

# 已对接第三方

<table>
<tr>
    <th></th>
    <th colspan="5"  align="center">流程相关</th>
    <th  colspan="5"  align="center">实例相关</th>
</tr>
<tr>
    <th></th>
    <th>创建</th>
    <th>删除</th>
    <th>修改</th>
    <th>详情</th>
    <th>分页</th>
    <th>创建</th>
    <th>删除</th>
    <th>修改</th>
    <th>详情</th>
    <th>分页</th>
</tr>
<tr>
    <td>钉钉</td>
    <td>不支持</td>
    <td>不支持</td>
    <td>不支持</td>
    <td>不支持</td>
    <td>支持</td>
    <td>支持</td>
    <td>不支持</td>
    <td>不支持</td>
    <td>支持</td>
    <td>不支持</td>
</tr>
<tr>
    <td>
        飞书
    </td>
    <td>不支持</td>
    <td>不支持</td>
    <td>不支持</td>
    <td>支持</td>
    <td>不支持</td>
    <td>支持</td>
    <td>不支持</td>
    <td>不支持</td>
    <td>支持</td>
    <td>不支持</td>
</tr>

<tr>
    <td>企业微信</td>
    <td colspan="10" align="center">计划开发中</td>
</tr>

<tr>
    <td>自研审批流</td>
    <td colspan="10" align="center">计划开发中</td>
</tr>
</table>
