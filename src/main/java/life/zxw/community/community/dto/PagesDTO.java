package life.zxw.community.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PagesDTO<T> {
    private Integer page;
    private boolean first_page;
    private boolean last_page;
    private boolean previous_page;
    private boolean next_page;
    private List<T> data;
    private List<Integer> pages = new ArrayList<>();
    private Integer page_count;

    public void setPages(Integer question_count,Integer page) {
        this.page_count = question_count;
        this.page = page;
        pages.add(page);

        for (int i = 1; i <= 3; i++) {
            if (page - i > 0) {
                pages.add(0, page - i);
            }
            if (page + i <= page_count) {
                pages.add(page + i);
            }
        }

        if (page == 1) {
            previous_page = false;
        } else {
            previous_page = true;
        }

        if (page == page_count) {
            next_page = false;
        } else {
            next_page = true;
        }

        if (pages.contains(1)) {
            first_page = false;
        } else {
            first_page = true;
        }

        if (pages.contains(page_count)) {
            last_page = false;
        } else {
            last_page = true;
        }


    }
}
