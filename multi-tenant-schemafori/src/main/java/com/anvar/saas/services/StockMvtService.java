package com.anvar.saas.services;

import com.anvar.saas.common.PageResponse;
import com.anvar.saas.requests.StockMvtRequest;
import com.anvar.saas.responses.StockMvtResponse;

public interface StockMvtService extends BasicService<StockMvtRequest, StockMvtResponse> {

    PageResponse<StockMvtResponse> findAllByProductId(final String productId, final int page, final int size);
}