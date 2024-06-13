#!/usr/bin/python

# Copyright The OpenTelemetry Authors
# SPDX-License-Identifier: Apache-2.0

import logger
import sys
from pythonjsonlogger import jsonlogger
from opentelemetry import trace


class CustomJsonFormatter(jsonlogger.JsonFormatter):
    def add_fields(self, log_record, record, message_dict):
        super(CustomJsonFormatter, self).add_fields(log_record, record, message_dict)
        if not log_record.get('otelTraceID'):
            log_record['otelTraceID'] = trace.format_trace_id(trace.get_current_span().get_span_context().trace_id)
        if not log_record.get('otelSpanID'):
            log_record['otelSpanID'] = trace.format_span_id(trace.get_current_span().get_span_context().span_id)

def getJSONLogger(name):
    logger = logger.getLogger(name)
    handler = logger.StreamHandler(sys.stdout)
    formatter = CustomJsonFormatter('%(asctime)s %(levelname)s [%(name)s] [%(filename)s:%(lineno)d] [trace_id=%(otelTraceID)s span_id=%(otelSpanID)s] - %(message)s')
    handler.setFormatter(formatter)
    logger.addHandler(handler)
    logger.setLevel(logger.INFO)
    logger.propagate = False
    return logger