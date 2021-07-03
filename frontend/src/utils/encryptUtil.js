const md5 = require('md5');

export function encryptMD5(text) {
    return md5(text)
}
