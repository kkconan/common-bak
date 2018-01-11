package com.money.game.core.constant;

import com.money.game.core.exception.BaseException;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * conan
 * 2017/10/26 13:56
 **/
@Data
@NoArgsConstructor
public class ResponseData implements Serializable{

    private static final long serialVersionUID = 4823505142935525041L;

    private static final String SUCCESS_CODE = "0"; //成功
    private static final String SUCCESS_MESSAGE = "success"; //成功

    private static final String FAILURE_CODE = "-1"; //失败
    private static final String FAILURE_MESSAGE = "failure"; //失败



    private String errorCode;
    private String errorMessage;
    private Result result;//数据

    /**
     * 成功返回值对象
     *
     * @return ResponseData
     */
    public static ResponseData success() {
        return new ResponseData(SUCCESS_CODE, SUCCESS_MESSAGE, null);
    }

    /**
     * 成功返回值对象
     *
     * @param data 数据
     * @return ResponseData
     */
    public static <T> ResponseData success(T data) {
        return new ResponseData(SUCCESS_CODE, SUCCESS_MESSAGE, new Result(data, null));
    }

    /**
     * 分页成功返回值对象
     *
     * @param data        数据
     * @param currentPage 当前页
     * @param pageSize    每页大小
     * @param totalCount  总行数
     * @return ResponseData
     */
    public static <T> ResponseData success(T data, Integer currentPage, Integer pageSize, Long totalCount) {
        return new ResponseData(SUCCESS_CODE, SUCCESS_MESSAGE, data, currentPage, pageSize, totalCount);
    }


    /**
     * 失败返回值对象
     *
     * @return ResponseData
     */
    public static ResponseData failure() {
        return new ResponseData(FAILURE_CODE, FAILURE_MESSAGE, null);
    }

    /**
     * 失败返回值对象
     *
     * @return ResponseData
     */
    public static ResponseData failure(BaseException e) {
        return new ResponseData(e.getCode(), e.getMessage(), null);
    }

    /**
     * 失败码/失败信息回值对象
     *
     * @return ResponseData
     */
    public static ResponseData failure(String errno, String msg) {
        return new ResponseData(errno, msg, null);
    }


    private <T> ResponseData(String errorCode, String errorMessage, T data, Integer currentPage, Integer pageSize, Long totalCount) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        if (currentPage != null && pageSize != null && totalCount != null) {
            this.result = new Result(data, new Page(currentPage, pageSize, totalCount));
            ;
        }
    }


    private ResponseData(String errorCode, String errorMessage, Result result) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.result = result;
    }

    public void setMeta(String errno, String msg) {
        this.errorCode = errno;
        this.errorMessage = msg;
    }

    public static boolean isFailure(ResponseData responseData){
        return !"0".equals(responseData.errorCode);
    }

    public static class Result<T> implements Serializable {
        private static final long serialVersionUID = 8836426613791659696L;
        private Page page;
        private T data;

        private Result() {
        }

        private Result(T data, Page page) {
            this.data = data;
            this.page = page;
        }

        public Page getPage() {
            return page;
        }


        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                    .append("page", page)
                    .append("data", data)
                    .toString();
        }
    }

    public static class Page implements Serializable {
        private static final long serialVersionUID = 1863202111039389798L;
        private Integer currentPage;//当前页：统一从1开始
        private Integer pageSize = 10; //每页行数
        private Long totalCount; //总行数

        private Page() {
        }

        private Page(Integer currentPage, Integer pageSize, Long totalCount) {
            this.currentPage = currentPage;
            this.pageSize = pageSize;
            this.totalCount = totalCount;
        }

        public Integer getCurrentPage() {
            return currentPage;
        }


        public Integer getPageSize() {
            return pageSize;
        }


        public Long getTotalCount() {
            return totalCount;
        }


        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                    .append("currentPage", currentPage)
                    .append("pageSize", pageSize)
                    .append("totalCount", totalCount)
                    .toString();
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("errorCode",errorCode)
                .append("errorMessage",errorMessage)
                .append("result", result)
                .toString();
    }
}
