export function isCharNumeric(char: string) {
    return char.length === 1 && char >= '0' && char <= '9';
}

export class Position {
    i: number;
    j: number;

    constructor(i: number, j: number) {
        this.i = i;
        this.j = j;
    }

    equals(other: Position): boolean {
        return other instanceof Position && this.i === other.i && this.j === other.j;
    }

    hashCode(): string {
        return `${this.i},${this.j}`;
    }

    toSort(other: Position): number {
        if (this.i !== other.i) {
            return this.i - other.i;
        }
        return this.j - other.j;
    }

    toString(): string {
        return `${this.i},${this.j}`
    }
}

export class PositionSet {
    private map: Map<string, Position> = new Map();

    add(position: Position): void {
        const hash: string = position.hashCode();

        if (!this.map.has(hash) || this.map.get(hash)?.equals(position) === false) {
            this.map.set(hash, position);
        }
    }

    has(position: Position): boolean {
        const storedPositon: Position | undefined = this.map.get(position.hashCode());
        return storedPositon !== undefined && storedPositon.equals(position);
    }

    delete(position: Position): boolean {
        return this.map.delete(position.hashCode());
    }

    get size(): number {
        return this.map.size;
    }

    get(position: Position): Position | undefined {
        return this.map.get(position.hashCode());
    }

    [Symbol.iterator](): Iterator<Position> {
        return this.map.values();
    }
}