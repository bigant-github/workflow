package org.bigant.wf.form.option;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 单选框附属信息
 *
 * @author galen
 * @date 2024/2/2311:21
 */
@ApiModel("多选框附属信息")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MultiSelectOption {

    private List<Option> options;

    //将options转换为map
    public Map<String, String> optionsToMap() {
        return options.stream().collect(Collectors.toMap(Option::getName, Option::getValue));
    }
    @Data
    @Builder
    @ApiModel("单选框附属信息-选项")
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Option {

        private String name;

        private String value;

    }




}
