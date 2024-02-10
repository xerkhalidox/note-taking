package org.rbs.api.v1;

import org.springframework.web.bind.annotation.RequestMapping;

import static org.rbs.data.constants.Apis.API_V1;

@RequestMapping(API_V1)
public abstract class AbstractController {
    static int DEFAULT_SIZE = 10;
}
