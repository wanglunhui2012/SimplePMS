package indi.simple.pms.entity.viewobject;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:20
 * @Description:
 */
public class SystemNavigationMenuMetaVO {
    private String title;
    private String icon;
    private Boolean noCache;
    private Boolean breadcrumb;
    private Boolean affix;

    public SystemNavigationMenuMetaVO() {
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getNoCache() {
        return this.noCache;
    }

    public void setNoCache(Boolean noCache) {
        this.noCache = noCache;
    }

    public Boolean getBreadcrumb() {
        return this.breadcrumb;
    }

    public void setBreadcrumb(Boolean breadcrumb) {
        this.breadcrumb = breadcrumb;
    }

    public Boolean getAffix() {
        return this.affix;
    }

    public void setAffix(Boolean affix) {
        this.affix = affix;
    }
}
