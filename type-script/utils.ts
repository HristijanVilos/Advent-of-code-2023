export function isCharNumeric(char: string) {
    return char.length === 1 && char >= '0' && char <= '9';
}