package com.gdesign.fisheyemoviesys.entity.dto;

import java.io.Serializable;
import java.util.List;

public class PageResultDTO<T> implements Serializable {

    private static final long serialVersionUID = -6649380283807435701L;

    private List<T> rows;
    private Long total;
    private Long page;
    private Long pageSize;
    private Long totalPage;

    public static <T> PageResultDTO.PageResultDTOBuilder<T> builder() {
        return new PageResultDTO.PageResultDTOBuilder();
    }

    public List<T> getRows() {
        return this.rows;
    }

    public Long getTotal() {
        return this.total;
    }

    public Long getPage() {
        return this.page;
    }

    public Long getPageSize() {
        return this.pageSize;
    }

    public Long getTotalPage() {
        return this.totalPage;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof PageResultDTO)) {
            return false;
        } else {
            PageResultDTO<?> other = (PageResultDTO)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label71: {
                    Object this$total = this.getTotal();
                    Object other$total = other.getTotal();
                    if (this$total == null) {
                        if (other$total == null) {
                            break label71;
                        }
                    } else if (this$total.equals(other$total)) {
                        break label71;
                    }

                    return false;
                }

                Object this$page = this.getPage();
                Object other$page = other.getPage();
                if (this$page == null) {
                    if (other$page != null) {
                        return false;
                    }
                } else if (!this$page.equals(other$page)) {
                    return false;
                }

                label57: {
                    Object this$pageSize = this.getPageSize();
                    Object other$pageSize = other.getPageSize();
                    if (this$pageSize == null) {
                        if (other$pageSize == null) {
                            break label57;
                        }
                    } else if (this$pageSize.equals(other$pageSize)) {
                        break label57;
                    }

                    return false;
                }

                Object this$totalPage = this.getTotalPage();
                Object other$totalPage = other.getTotalPage();
                if (this$totalPage == null) {
                    if (other$totalPage != null) {
                        return false;
                    }
                } else if (!this$totalPage.equals(other$totalPage)) {
                    return false;
                }

                Object this$rows = this.getRows();
                Object other$rows = other.getRows();
                if (this$rows == null) {
                    if (other$rows == null) {
                        return true;
                    }
                } else if (this$rows.equals(other$rows)) {
                    return true;
                }

                return false;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof PageResultDTO;
    }

    @Override
    public int hashCode() {
        int result = 1;
        Object $total = this.getTotal();
        result = result * 59 + ($total == null ? 43 : $total.hashCode());
        Object $page = this.getPage();
        result = result * 59 + ($page == null ? 43 : $page.hashCode());
        Object $pageSize = this.getPageSize();
        result = result * 59 + ($pageSize == null ? 43 : $pageSize.hashCode());
        Object $totalPage = this.getTotalPage();
        result = result * 59 + ($totalPage == null ? 43 : $totalPage.hashCode());
        Object $rows = this.getRows();
        result = result * 59 + ($rows == null ? 43 : $rows.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "PageResultDTO(rows=" + this.getRows() + ", total=" + this.getTotal() + ", page=" + this.getPage() + ", pageSize=" + this.getPageSize() + ", totalPage=" + this.getTotalPage() + ")";
    }

    public PageResultDTO(List<T> rows, Long total, Long page, Long pageSize, Long totalPage) {
        this.rows = rows;
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
    }

    public PageResultDTO() {
    }

    public static class PageResultDTOBuilder<T> {
        private List<T> rows;
        private Long total;
        private Long page;
        private Long pageSize;
        private Long totalPage;

        PageResultDTOBuilder() {
        }

        public PageResultDTO.PageResultDTOBuilder<T> rows(List<T> rows) {
            this.rows = rows;
            return this;
        }

        public PageResultDTO.PageResultDTOBuilder<T> total(Long total) {
            this.total = total;
            return this;
        }

        public PageResultDTO.PageResultDTOBuilder<T> page(Long page) {
            this.page = page;
            return this;
        }

        public PageResultDTO.PageResultDTOBuilder<T> pageSize(Long pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public PageResultDTO.PageResultDTOBuilder<T> totalPage(Long totalPage) {
            this.totalPage = totalPage;
            return this;
        }

        public PageResultDTO<T> build() {
            return new PageResultDTO(this.rows, this.total, this.page, this.pageSize, this.totalPage);
        }

        @Override
        public String toString() {
            return "PageResultDTO.PageResultDTOBuilder(rows=" + this.rows + ", total=" + this.total + ", page=" + this.page + ", pageSize=" + this.pageSize + ", totalPage=" + this.totalPage + ")";
        }
    }

}
