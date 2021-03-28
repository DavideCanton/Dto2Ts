package com.mdcc.dto2ts.core.utils;

import java.util.*;
import java.util.stream.*;

public class StreamUtils
{
    public static <T> Stream<T> toStream(Optional<T> opt)
    {
        return opt.map(Stream::of).orElseGet(Stream::empty);
    }
}
