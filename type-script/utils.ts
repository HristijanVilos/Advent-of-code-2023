export function isCharNumeric(char: string) {
    return char.length === 1 && char >= '0' && char <= '9';
}

export const DIRECTION = {
    EAST: [0, 1] as const,
    WEST: [0, -1] as const,
    SOUTH: [1, 0] as const,
    NORTH: [-1, 0] as const,
};

export type DirectionType = typeof DIRECTION[keyof typeof DIRECTION];

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

interface Hashable {
    hashCode(): string;
}

export class CustomSet<T extends Hashable> {
    private map: Map<string, T> = new Map();

    add(item: T): void {
        const hash = item.hashCode();
        if (!this.map.has(hash)) {
            this.map.set(hash, item);
        }
    }

    has(item: T): boolean {
        const hash = item.hashCode();
        return this.map.has(hash);
    }

    delete(item: T): boolean {
        const hash = item.hashCode();
        return this.map.delete(hash);

    }

    get size(): number {
        return this.map.size;
    }

    get(item: T): T | undefined {
        return this.map.get(item.hashCode());
    }

    [Symbol.iterator](): Iterator<T> {
        return this.map.values();
    }
}

interface ValueOf {
    valueOf(): number;
}


export class MinHeap<T extends ValueOf> {
    public length: number;
    public data: T[];

    constructor() {
        this.length = 0;
        this.data = [];
    }


    insert(item: T): void {
        this.data.push(item);
        this.length++;
        this.heapifyUp(this.length - 1);
        
    }


    pop(): T | null {
        if (this.length === 0) {
            return null;
        }

        const out: T = this.data[0];
        this.length--;

        if (this.length === 0) {
            this.data = [];
            return out;
        }

        this.data[0] = this.data.pop()!;
        this.heapifyDown(0);
        return out;
    }


    private heapifyDown(idx: number): void {
        const lIdx: number = this.getLeftChildId(idx);
        const rIdx: number = this.getRightChildId(idx);

        if (lIdx >= this.length) {
            return;
        }

        const left: T = this.data[lIdx];
        const leftValue: number = left.valueOf();

        let smallestIdx: number = lIdx;
        let smallest: T = left;
        let smallestValue: number = leftValue;

        if (rIdx < this.length) {
            const right: T = this.data[rIdx];
            const rightValue: number = right.valueOf();

            if (rightValue < smallestValue) {
                smallestIdx = rIdx;
                smallest = right;
                smallestValue = rightValue;
            }
        }

        const current: T = this.data[idx];
        const currentValue: number = current.valueOf();

        if (currentValue > smallestValue) {
            this.data[smallestIdx] = current;
            this.data[idx] = smallest;
            this.heapifyDown(smallestIdx);
        }
    }



    private heapifyUp(idx: number): void {
        if (idx === 0) {
            return;
        }

        const pIdx: number = this.getParrentId(idx);
        // parrent
        const parent: T = this.data[pIdx];
        const parentValue: number = parent.valueOf();
        // current
        const current: T = this.data[idx];
        const currentValue: number = current.valueOf();

        if (parentValue > currentValue) {
            this.data[pIdx] = current;
            this.data[idx] = parent;
            return this.heapifyUp(pIdx);
        }
    }

    private getLeftChildId(idx: number): number {
        return (idx * 2) + 1;
    }

    private getRightChildId(idx: number): number {
        return (idx * 2) + 2;
    }

    private getParrentId(idx: number): number {
        return Math.floor((idx - 1) / 2);
    }
}