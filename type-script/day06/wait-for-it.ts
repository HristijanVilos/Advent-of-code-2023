import * as fs from "fs";

const f: string = fs.readFileSync("../inputs/day06/input.txt", { encoding: "utf-8" });
const timeAndDistanceRaw: string[] = f.split("\n").map(x => x.split(":")[1].trim());
const timeAndDistance: number[][] = timeAndDistanceRaw.map(x => x.split(/\s+/).map(x => Number(x)));
const timeAndDistancePart2: number[] = timeAndDistanceRaw.map(x => Number(x.split(/\s+/).join("")));

function part1(timeAndDistance: number[][]): number {
    let result: number = 1;
    const time: number[] = timeAndDistance[0];
    const distance: number[] = timeAndDistance[1];
    for (let race = 0; race < time.length; race++) {
        const sucesfullRace = numberOfWaysToWin(time[race], distance[race]);
        result *= sucesfullRace;
    }
    return result;
}

function part2(timeAndDistancePart2: number[]): number {
    let result: number = 1;

    const sucesfullRace = numberOfWaysToWin(timeAndDistancePart2[0], timeAndDistancePart2[1]);
    result *= sucesfullRace;
    return result;
}

function numberOfWaysToWin(raceTime: number, raceDistance: number) {
    let sucesfullRace: number = 0;
    for (let i = 0; i < raceTime; i++) {
        const remainingTime: number = raceTime - i;
        if (i * remainingTime > raceDistance) {
            sucesfullRace++;
        }
    }
    return sucesfullRace;
}

console.log("Part 1: ", part1(timeAndDistance));
console.log("Part 2: ", part2(timeAndDistancePart2));
