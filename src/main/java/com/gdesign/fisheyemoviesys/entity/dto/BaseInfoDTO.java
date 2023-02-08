package com.gdesign.fisheyemoviesys.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ycy
 */
public class BaseInfoDTO implements Serializable {
    private static final long serialVersionUID = 8778450239008611161L;

    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modifyTime;
    private Long modifyId;
    private Integer deleted;
    private Long version;

    public BaseInfoDTO() {
    }

    public Long getId() {
        return this.id;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public Date getModifyTime() {
        return this.modifyTime;
    }

    public Long getModifyId() {
        return this.modifyId;
    }

    public Integer getDeleted() {
        return this.deleted;
    }

    public Long getVersion() {
        return this.version;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public void setModifyId(Long modifyId) {
        this.modifyId = modifyId;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof BaseInfoDTO)) {
            return false;
        } else {
            BaseInfoDTO other = (BaseInfoDTO) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label95:
                {
                    Object this$id = this.getId();
                    Object other$id = other.getId();
                    if (this$id == null) {
                        if (other$id == null) {
                            break label95;
                        }
                    } else if (this$id.equals(other$id)) {
                        break label95;
                    }

                    return false;
                }

                Object this$modifyId = this.getModifyId();
                Object other$modifyId = other.getModifyId();
                if (this$modifyId == null) {
                    if (other$modifyId != null) {
                        return false;
                    }
                } else if (!this$modifyId.equals(other$modifyId)) {
                    return false;
                }

                label74:
                {
                    Object this$deleted = this.getDeleted();
                    Object other$deleted = other.getDeleted();
                    if (this$deleted == null) {
                        if (other$deleted == null) {
                            break label74;
                        }
                    } else if (this$deleted.equals(other$deleted)) {
                        break label74;
                    }

                    return false;
                }

                label67:
                {
                    Object this$version = this.getVersion();
                    Object other$version = other.getVersion();
                    if (this$version == null) {
                        if (other$version == null) {
                            break label67;
                        }
                    } else if (this$version.equals(other$version)) {
                        break label67;
                    }

                    return false;
                }

                Object this$createTime = this.getCreateTime();
                Object other$createTime = other.getCreateTime();
                if (this$createTime == null) {
                    if (other$createTime != null) {
                        return false;
                    }
                } else if (!this$createTime.equals(other$createTime)) {
                    return false;
                }

                Object this$modifyTime = this.getModifyTime();
                Object other$modifyTime = other.getModifyTime();
                if (this$modifyTime == null) {
                    if (other$modifyTime != null) {
                        return false;
                    }
                } else if (!this$modifyTime.equals(other$modifyTime)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof BaseInfoDTO;
    }

    @Override
    public int hashCode() {
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        Object $modifyId = this.getModifyId();
        result = result * 59 + ($modifyId == null ? 43 : $modifyId.hashCode());
        Object $deleted = this.getDeleted();
        result = result * 59 + ($deleted == null ? 43 : $deleted.hashCode());
        Object $version = this.getVersion();
        result = result * 59 + ($version == null ? 43 : $version.hashCode());
        Object $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
        Object $modifyTime = this.getModifyTime();
        result = result * 59 + ($modifyTime == null ? 43 : $modifyTime.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "BaseInfoDTO(id=" + this.getId() + ", createTime=" + this.getCreateTime() + ", modifyTime=" + this.getModifyTime() + ", modifyId=" + this.getModifyId() + ", deleted=" + this.getDeleted() + ", version=" + this.getVersion() + ")";
    }
}
