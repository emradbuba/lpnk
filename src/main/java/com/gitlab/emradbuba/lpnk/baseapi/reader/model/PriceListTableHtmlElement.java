package com.gitlab.emradbuba.lpnk.baseapi.reader.model;

import lombok.Builder;
import lombok.Getter;
import org.jsoup.nodes.Element;

@Getter
@Builder
public
class PriceListTableHtmlElement {
    private final int rowNumber;
    private final int columnNumber;
    private final Element htmlElement;
}
