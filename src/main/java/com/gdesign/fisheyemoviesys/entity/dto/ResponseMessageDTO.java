package com.gdesign.fisheyemoviesys.entity.dto;

import com.gdesign.fisheyemoviesys.entity.enums.CodeEnum;

import java.io.Serializable;

/**
 * @author ycy
 */
public class ResponseMessageDTO<T> implements Serializable {
    private static final long serialVersionUID = -2968182029248805504L;

    private Integer code;
    private String message;
    private T result;
    private Boolean success;

    public ResponseMessageDTO(CodeEnum codeEnum) {
        this.code = codeEnum.getCode();
        this.message = codeEnum.getDesc();
        this.success = Boolean.FALSE;
    }

    public ResponseMessageDTO(T data) {
        this.code = CodeEnum.SUCCESS.getCode();
        this.success = Boolean.TRUE;
        this.result = data;
    }

    public ResponseMessageDTO() {
        this.success = Boolean.TRUE;
    }

    public static ResponseMessageDTO error(CodeEnum codeEnum) {
        return new ResponseMessageDTO(codeEnum);
    }

    public static <T> ResponseMessageDTO<T> success(T data) {
        return new ResponseMessageDTO(data);
    }

    public static <T> ResponseMessageDTO.ResponseMessageDTOBuilder<T> builder() {
        return new ResponseMessageDTO.ResponseMessageDTOBuilder();
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public T getResult() {
        return this.result;
    }

    public Boolean getSuccess() {
        return this.success;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ResponseMessageDTO)) {
            return false;
        } else {
            ResponseMessageDTO<?> other = (ResponseMessageDTO) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label59:
                {
                    Object this$code = this.getCode();
                    Object other$code = other.getCode();
                    if (this$code == null) {
                        if (other$code == null) {
                            break label59;
                        }
                    } else if (this$code.equals(other$code)) {
                        break label59;
                    }

                    return false;
                }

                Object this$success = this.getSuccess();
                Object other$success = other.getSuccess();
                if (this$success == null) {
                    if (other$success != null) {
                        return false;
                    }
                } else if (!this$success.equals(other$success)) {
                    return false;
                }

                Object this$message = this.getMessage();
                Object other$message = other.getMessage();
                if (this$message == null) {
                    if (other$message != null) {
                        return false;
                    }
                } else if (!this$message.equals(other$message)) {
                    return false;
                }

                Object this$result = this.getResult();
                Object other$result = other.getResult();
                if (this$result == null) {
                    if (other$result != null) {
                        return false;
                    }
                } else if (!this$result.equals(other$result)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof ResponseMessageDTO;
    }

    @Override
    public int hashCode() {
        int result = 1;
        Object $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : $code.hashCode());
        Object $success = this.getSuccess();
        result = result * 59 + ($success == null ? 43 : $success.hashCode());
        Object $message = this.getMessage();
        result = result * 59 + ($message == null ? 43 : $message.hashCode());
        Object $result = this.getResult();
        result = result * 59 + ($result == null ? 43 : $result.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "ResponseMessageDTO(code=" + this.getCode() + ", message=" + this.getMessage() + ", result=" + this.getResult() + ", success=" + this.getSuccess() + ")";
    }

    public ResponseMessageDTO(Integer code, String message, T result, Boolean success) {
        this.code = code;
        this.message = message;
        this.result = result;
        this.success = success;
    }

    public static class ResponseMessageDTOBuilder<T> {
        private Integer code;
        private String message;
        private T result;
        private Boolean success;

        ResponseMessageDTOBuilder() {
        }

        public ResponseMessageDTO.ResponseMessageDTOBuilder<T> code(Integer code) {
            this.code = code;
            return this;
        }

        public ResponseMessageDTO.ResponseMessageDTOBuilder<T> message(String message) {
            this.message = message;
            return this;
        }

        public ResponseMessageDTO.ResponseMessageDTOBuilder<T> result(T result) {
            this.result = result;
            return this;
        }

        public ResponseMessageDTO.ResponseMessageDTOBuilder<T> success(Boolean success) {
            this.success = success;
            return this;
        }

        public ResponseMessageDTO<T> build() {
            return new ResponseMessageDTO(this.code, this.message, this.result, this.success);
        }

        @Override
        public String toString() {
            return "ResponseMessageDTO.ResponseMessageDTOBuilder(code=" + this.code + ", message=" + this.message + ", result=" + this.result + ", success=" + this.success + ")";
        }
    }
}
