package com.kepware.opc.thread.monitor;/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2009 TH4 SYSTEMS GmbH (http://th4-systems.com)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

import com.www.util.LoggerUtil;
import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.*;

public class VariantDumper {

    static protected void dumpArray(final String prefix, final JIArray array) throws JIException {
        LoggerUtil.getLoggerByName("opcMonitor").info(prefix + String.format("IsConformant: %s, IsVarying: %s", array.isConformant() ? "yes" : "no", array.isVarying() ? "yes" : "no"));
        LoggerUtil.getLoggerByName("opcMonitor").info(prefix + String.format("Dimensions: %d", array.getDimensions()));
        for (int i = 0; i < array.getDimensions(); i++) {
            LoggerUtil.getLoggerByName("opcMonitor").info(prefix + String.format("Dimension #%d: Upper Bound: %d", i, array.getUpperBounds()[i]));
        }

        final Object o = array.getArrayInstance();
        LoggerUtil.getLoggerByName("opcMonitor").info(prefix + "Array Instance: " + o.getClass());
        final Object[] a = (Object[]) o;
        LoggerUtil.getLoggerByName("opcMonitor").info(prefix + "Array Size: " + a.length);

        for (final Object value : a) {
            dumpValue(prefix + "\t", value);
        }
    }

    static public void dumpValue(final Object value) throws JIException {
        dumpValue("", value);
    }

    static protected String dumpValue(final String prefix, final Object value) throws JIException {
        if (value instanceof JIVariant) {
            final JIVariant variant = (JIVariant) value;
            if (variant.isArray()) {
                dumpArray(prefix, variant.getObjectAsArray());
            } else {
                dumpValue(prefix + "\t", variant.getObject());
            }
        } else if (value instanceof JIString) {
            final JIString string = (JIString) value;
            String strType;
            switch (string.getType()) {
                case JIFlags.FLAG_REPRESENTATION_STRING_BSTR:
                    strType = "BSTR";
                    break;
                case JIFlags.FLAG_REPRESENTATION_STRING_LPCTSTR:
                    strType = "LPCSTR";
                    break;
                case JIFlags.FLAG_REPRESENTATION_STRING_LPWSTR:
                    strType = "LPWSTR";
                    break;
                default:
                    strType = "unknown";
                    break;
            }
        } else if (value instanceof Double) {
            return Double.valueOf((Double) value) + "";
        } else if (value instanceof Float) {
            return Float.valueOf((Float) value) + "";
        } else if (value instanceof Byte) {
            return Float.valueOf((Float) value) + "";
        } else if (value instanceof Character) {
            return Float.valueOf((Float) value) + "";
        } else if (value instanceof Integer) {
            return Float.valueOf((Float) value) + "";
        } else if (value instanceof Short) {
            return Float.valueOf((Float) value) + "";
        } else if (value instanceof Long) {
            return Float.valueOf((Float) value) + "";
        } else if (value instanceof Boolean) {
            return value + "";
        } else if (value instanceof IJIUnsigned) {
            return ((IJIUnsigned) value).getValue().toString();
        } else {
            return null;
        }
        return null;
    }

}
