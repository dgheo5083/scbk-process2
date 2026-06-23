package com.scbank.process.api.svc.shared.components.ars.utils;

public class SyncAtByteUtils {

    public static void makeLongMsg(byte[] _msg, int _offset, long _data) {
        _msg[_offset++] = (byte) (_data >>> 56);
        _msg[_offset++] = (byte) ((_data << 8) >>> 56);
        _msg[_offset++] = (byte) ((_data << 16) >>> 56);
        _msg[_offset++] = (byte) ((_data << 24) >>> 56);
        _msg[_offset++] = (byte) ((_data << 32) >>> 56);
        _msg[_offset++] = (byte) ((_data << 40) >>> 56);
        _msg[_offset++] = (byte) ((_data << 48) >>> 56);
        _msg[_offset++] = (byte) ((_data << 56) >>> 56);
    }

    public static void makeIntMsg(byte[] _msg, int _offset, int _data) {
        _msg[_offset++] = (byte) (_data >>> 24);
        _msg[_offset++] = (byte) ((_data << 8) >>> 24);
        _msg[_offset++] = (byte) ((_data << 16) >>> 24);
        _msg[_offset++] = (byte) ((_data << 24) >>> 24);
    }

    public static void makeIntMsg2(byte[] _msg, int _offset, int _data) {
        _msg[_offset++] = (byte) ((_data << 24) >>> 24);
        _msg[_offset++] = (byte) ((_data << 16) >>> 24);
        _msg[_offset++] = (byte) ((_data << 8) >>> 24);
        _msg[_offset++] = (byte) (_data >>> 24);
    }

    public static void makeShortMsg(byte[] _msg, int _offset, short _data) {
        _msg[_offset++] = (byte) (_data >>> 8);
        _msg[_offset++] = (byte) ((_data << 8) >>> 8);
    }

    public static void makeBytesMsg(
            byte[] _msg,
            int _offset,
            byte[] _data,
            int _len) {

        if (_data == null) {
            makeNullBytes(_msg, _offset, _len);
            return;
        }

        for (int i = 0; i < _data.length; i++) {
            _msg[_offset++] = _data[i];
        }

        if (_data.length < _len) {
            for (int i = 0; i < (_len - _data.length); i++) {
                _msg[_offset++] = (byte) 0x00;
            }
        }
    } // end makeBytesMsg()

    public static void makeBytes20Msg(
            byte[] _msg,
            int _offset,
            byte[] _data,
            int _len) {

        if (_data == null) {
            makeNullBytes(_msg, _offset, _len);
            return;
        }

        for (int i = 0; i < _data.length; i++) {
            _msg[_offset++] = _data[i];
        }

        if (_data.length < _len) {
            for (int i = 0; i < (_len - _data.length); i++) {
                _msg[_offset++] = (byte) 0x20;
            }
        }
    } // end makeBytes20Msg()

    public static void makeBytesDecimal20Msg(
            byte[] _msg,
            int _offset,
            byte[] _data,
            int _len) {

        if (_data == null) {
            makeNullBytes(_msg, _offset, _len);
            return;
        }

        if (_data.length < _len) {
            for (int i = 0; i < (_len - _data.length); i++) {
                _msg[_offset++] = (byte) 0x20;
            }
        }

        for (int i = 0; i < _data.length; i++) {
            _msg[_offset++] = _data[i];
        }

    } // end makeBytes20Msg()

    public static void makeBytes30Msg(
            byte[] _msg,
            int _offset,
            byte[] _data,
            int _len) {

        if (_data == null) {
            makeNullBytes(_msg, _offset, _len);
            return;
        }

        if (_data.length < _len) {
            for (int i = 0; i < (_len - _data.length); i++) {
                _msg[_offset++] = (byte) 0x30;
            }
        }

        for (int i = 0; i < _data.length; i++) {
            _msg[_offset++] = _data[i];
        }
    } // end makeBytes20Msg()

    /** 중국의 스펙에 맞추기 위해 개발 */
    public static void makeOctetBytesMsg(
            byte[] _msg,
            int _offset,
            byte[] _data,
            int _len) {

        if (_data == null) {
            makeNullBytes(_msg, _offset, _len);
            return;
        }

        if (_data.length < _len) {
            for (int i = 0; i < (_len - _data.length); i++) {
                // _msg[_offset++] = (byte) 0x20;
                _msg[_offset++] = (byte) 0x30;
            }
        }

        for (int i = 0; i < _data.length; i++) {
            _msg[_offset++] = _data[i];
        }

    } // end makeBytesMsg()

    public static void makeByteMsg(byte[] _msg, int _offset, byte _data) {
        _msg[_offset++] = _data;
    } // end makeByteMsg

    public static void makeNullBytes(byte[] _msg, int _offset, int _len) {
        for (int i = 0; i < _len; i++) {
            _msg[_offset++] = 0;
        }
    } // end makeNullBytes

    /**********************************************************************************************
     * 이하 소켓으로 읽어들인 데이터를 프로토콜에 맞게 분석하는 메소드들 *
     **********************************************************************************************/

    public static long readLong(byte[] _data, int _offset) {
        return (((long)_data[_offset++] << 56)
                | (((long)_data[_offset++] << 56) >>> 8)
                | (((long)_data[_offset++] << 56) >>> 16)
                | (((long)_data[_offset++] << 56) >>> 24)
                | (((long)_data[_offset++] << 56) >>> 32)
                | (((long)_data[_offset++] << 56) >>> 40)
                | (((long)_data[_offset++] << 56) >>> 48)
                | (((long)_data[_offset++] << 56) >>> 56));
    }

    public static int readInt(byte[] _data, int _offset) {
        return ((_data[_offset++] << 24)
                | ((_data[_offset++] << 24) >>> 8)
                | ((_data[_offset++] << 24) >>> 16)
                | ((_data[_offset++] << 24) >>> 24));
    }

    public static short readShort(byte[] _data, int _offset) {
        return (short) ((_data[_offset++] << 8) | ((_data[_offset++] << 8) >>> 8));
    }

    public static byte[] readBytes(byte[] _data, int _offset, int _size) {
        byte[] result = new byte[_size];

        for (int i = 0; i < _size; i++) {
            result[i] = (byte) _data[_offset++];
        }

        return result;
    } // end readBytes()

    public static byte[] readOctetBytes(byte[] _data, int _offset, int _size) {
        byte[] result = new byte[_size];

        for (int i = _size - _data.length; i < _size; i++) {
            result[i] = (byte) _data[_offset++];
        }

        return result;
    } // end readBytes()

    /**
     * 소켓에서 읽어들인 데이타로 부터 한 바이트를 읽기 위한 메소프.
     * <p>
     * 
     * @param _data:   소켓에서 읽어들인 전체 데이타
     * @param _offset: 각 바이트가 세팅되어야 할 위치를 나타내는 포인트
     * @return: 데이타로부터 읽어들인 바이트
     */
    public static byte readByte(byte[] _data, int _offset) {
        // byte result = 0x00;
        // result = (byte) _data[_offset++];
        // return result;
        return (byte) _data[_offset++];
    }

    public static String toHexaString(int trans) {
        String temp = Integer.toHexString(trans);
        if (temp.length() == 1) {
            temp = "0" + temp;
        }
        return temp;
    }

    public static String writeHexaLog(byte[] msg, boolean writeChar) {
        StringBuffer sb = new StringBuffer();
        String hexaCode = "";
        String character = "";
        // sb.append("\n");
        for (int i = 0; i < msg.length; i += 16) {
            for (int j = i; j < i + 16; j++) {
                if (j < msg.length) {
                    hexaCode += toHexaString(msg[j]) + " ";
                    if (writeChar) {
                        character += toCharacter(msg[j]);
                    }
                } else {
                    hexaCode += "  " + " ";
                }
            }
            if (writeChar) {
                sb.append(
                        calucLineNumber(i)
                                + "\t"
                                + hexaCode
                                + "\t"
                                + character
                                + "\n");
            } else {
                sb.append(calucLineNumber(i) + "\t" + hexaCode + "\n");
            }
            hexaCode = "";
            character = "";
        }

        return sb.toString();
    }

    public static String writeHexaLog(byte[] msg) {
        return writeHexaLog(msg, true);
    }

    public static char toCharacter(byte b) {
        int digit = b;
        char temp = 0x00;
        if ((digit < 32) || (digit > 126)) {
            temp = '.';
        } else {
            temp = (char) digit;
        }
        return temp;
    }

    public static String toHexaString(byte b) {
        String hexa = Integer.toHexString(b);
        String temp = hexa;
        if (temp.length() > 2) {
            temp = hexa.substring(hexa.length() - 2, hexa.length());
        }

        if (temp.length() == 1) {
            temp = "0" + temp;
        }
        return temp.toUpperCase();
    }

    public static String calucLineNumber(int lineNumber) {
        String hexa = Integer.toHexString(lineNumber);

        for (int i = 0; i < (4 - hexa.length()); i++) {
            hexa = "0" + hexa;
        }

        if (hexa.length() < 4) {
            hexa = "0" + hexa;
        }

        return hexa.toUpperCase();
    }
    
//    public static void main(String[] args) {
//        byte[] haha = new byte[8];
//        SyncAtByteUtils.makeBytes30Msg(haha, 0, "3".getBytes(), 8);
//    }

}
