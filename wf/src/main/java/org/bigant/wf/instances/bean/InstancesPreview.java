package org.bigant.wf.instances.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.bigant.wf.form.component.ComponentType;
import org.bigant.wf.form.component.bean.Attachment;
import org.bigant.wf.form.component.bean.DateRange;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 发起审批实例实体
 *
 * @author galen
 * @date 2024/1/3114:54
 */
@Data
@ApiModel("发起审批实例")
public class InstancesPreview {
    @ApiModelProperty("审批实例code")
    private String code;

    @ApiModelProperty("发起人id")
    private String userId;

    @ApiModelProperty("发起人id")
    private String deptId;

    @ApiModelProperty("自选节点用户（与 targetSelectUsersAuthMatch 只能二选一）")
    private List<TargetSelectUser> targetSelectUsers;

    @ApiModelProperty("自选节点用户自动匹配 （与 targetSelectUsers 只能二选一）")
    private List<TargetSelectUserAuthMatch> targetSelectUsersAuthMatch;

    @ApiModelProperty("表单字段集合")
    private List<FormComponent> formComponents;


    @ApiModel("自选节点用户")
    @Data
    public static class TargetSelectUser {
        @ApiModelProperty("节点key")
        private String key;
        @ApiModelProperty("用户id")
        private List<String> userIds;
    }

    @Data
    @ApiModel("自选节点用户自动匹配")
    public static class TargetSelectUserAuthMatch {

        @ApiModelProperty("用户id")
        private List<String> userIds;
    }

    @ApiModel("表单字段")
    @Data
    public static class FormComponent {

        @ApiModelProperty("表单字段名称")
        private String name;

        @ApiModelProperty("表单字段值")
        private String value;

        @ApiModelProperty("表单字段类型")
        private ComponentType componentType;


        public FormComponent setName(String name) {
            this.name = name;
            return this;
        }

        public FormComponent setValue(String value) {
            this.setValue(value, ComponentType.TEXT);
            return this;
        }


        public FormComponent setTextareaValue(String value) {
            this.setValue(value, ComponentType.TEXTAREA);
            return this;
        }


        public FormComponent setNumberValue(Number value) {
            this.setValue(value, ComponentType.NUMBER);
            return this;
        }


        public FormComponent setSelectValue(String value) {
            this.setValue(value, ComponentType.SELECT);
            return this;
        }

        public FormComponent setMultiSelectValue(Collection<String> value) {
            this.setValue(value, ComponentType.MULTI_SELECT);
            return this;
        }

        public FormComponent setAttachmentValue(Collection<Attachment> value) {
            this.setValue(value, ComponentType.ATTACHMENT);
            return this;
        }


        public FormComponent setDateValue(LocalDateTime value) {
            this.setValue(value, ComponentType.DATE);
            return this;
        }

        public FormComponent setDateRangeValue(DateRange value) {
            this.setValue(value, ComponentType.DATE_RANGE);
            return this;
        }

        public FormComponent setDateRangeValue(LocalDateTime start, LocalDateTime end) {
            this.setValue(new DateRange(start, end), ComponentType.DATE_RANGE);
            return this;
        }


        private void setValue(Object value, ComponentType componentType) {
            this.componentType = componentType;
            this.value = componentType
                    .getParse()
                    .toStr(value);
        }


    }

}
