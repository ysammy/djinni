// AUTOGENERATED FILE - DO NOT MODIFY!
// This file generated by Djinni from all.djinni

import { PersistingState } from "proto/ts/prototest2"
import { AddressBook, Person } from "proto/ts/prototest"
import { Outcome } from "@djinni_support/Outcome"

export interface /*record*/ RecordWithEmbeddedProto {
    person: Person;
}

export interface /*record*/ RecordWithEmbeddedCppProto {
    state: PersistingState;
}

export interface ProtoTests {
}
export interface ProtoTests_statics {
    protoToStrings(x: AddressBook): Array<string>;
    stringsToProto(x: Array<string>): AddressBook;
    embeddedProtoToString(x: RecordWithEmbeddedProto): string;
    stringToEmbeddedProto(x: string): RecordWithEmbeddedProto;
    cppProtoToString(x: PersistingState): string;
    stringToCppProto(x: string): PersistingState;
    embeddedCppProtoToString(x: RecordWithEmbeddedCppProto): string;
    stringToEmbeddedCppProto(x: string): RecordWithEmbeddedCppProto;
    protoListToStrings(x: Array<Person>): Array<string>;
    stringsToProtoList(x: Array<string>): Array<Person>;
    optionalProtoToString(x: Person): string;
    stringToOptionalProto(x: string): Person;
    stringToProtoOutcome(x: string): Outcome<Person, number>;
}

export interface /*record*/ NestedOutcome {
    o: Outcome<number, string>;
}

export interface TestOutcome {
}
export interface TestOutcome_statics {
    getSuccessOutcome(): Outcome<string, number>;
    getErrorOutcome(): Outcome<string, number>;
    putSuccessOutcome(x: Outcome<string, number>): string;
    putErrorOutcome(x: Outcome<string, number>): number;
    getNestedSuccessOutcome(): NestedOutcome;
    getNestedErrorOutcome(): NestedOutcome;
    putNestedSuccessOutcome(x: NestedOutcome): number;
    putNestedErrorOutcome(x: NestedOutcome): string;
}

export interface TestDuration {
}
export interface TestDuration_statics {
    hoursString(dt: number): string;
    minutesString(dt: number): string;
    secondsString(dt: number): string;
    millisString(dt: number): string;
    microsString(dt: number): string;
    nanosString(dt: number): string;
    hours(count: number): number;
    minutes(count: number): number;
    seconds(count: number): number;
    millis(count: number): number;
    micros(count: number): number;
    nanos(count: number): number;
    hoursf(count: number): number;
    minutesf(count: number): number;
    secondsf(count: number): number;
    millisf(count: number): number;
    microsf(count: number): number;
    nanosf(count: number): number;
    box(count: bigint): number;
    unbox(dt: number): bigint;
}

export interface /*record*/ RecordWithDurationAndDerivings {
    dt: number;
}

export interface /*record*/ DateRecord {
    createdAt: Date;
}

export interface /*record*/ MapDateRecord {
    datesById: Map<string, Date>;
}

export interface DataRefTest {
    sendData(data: Uint8Array): void;
    retriveAsBin(): Uint8Array;
    sendMutableData(data: Uint8Array): void;
    generateData(): Uint8Array;
    dataFromVec(): Uint8Array;
    dataFromStr(): Uint8Array;
    sendDataView(data: Uint8Array): Uint8Array;
    recvDataView(): Uint8Array;
}
export interface DataRefTest_statics {
    create(): DataRefTest;
}

export const enum ConstantEnum {
    SOME_VALUE = 0,
    SOME_OTHER_VALUE = 1,
}

export interface /*record*/ ConstantWithEnum {
}

export interface ConstantInterfaceWithEnum {
}

export const enum AccessFlags {
    NOBODY = 0,
    OWNER_READ = 1 << 0,
    OWNER_WRITE = 1 << 1,
    OWNER_EXECUTE = 1 << 2,
    GROUP_READ = 1 << 3,
    GROUP_WRITE = 1 << 4,
    GROUP_EXECUTE = 1 << 5,
    SYSTEM_READ = 1 << 6,
    SYSTEM_WRITE = 1 << 7,
    SYSTEM_EXECUTE = 1 << 8,
    EVERYBODY = (1 << 9) - 1,
}

export const enum EmptyFlags {
    NONE = 0,
    ALL = (1 << 0) - 1,
}

export interface FlagRoundtrip {
}
export interface FlagRoundtrip_statics {
    roundtripAccess(flag: AccessFlags): AccessFlags;
    roundtripEmpty(flag: EmptyFlags): EmptyFlags;
    roundtripAccessBoxed(flag: AccessFlags): AccessFlags;
    roundtripEmptyBoxed(flag: EmptyFlags): EmptyFlags;
}

export interface /*record*/ RecordWithFlags {
    access: AccessFlags;
}

export interface /*record*/ SupportCopying {
    x: number;
}

export interface /*record*/ Vec2 {
    x: number;
    y: number;
}

export interface TestArray {
}
export interface TestArray_statics {
    testStringArray(a: Array<string>): Array<string>;
    testIntArray(a: Int32Array): Int32Array;
    testRecordArray(a: Array<Vec2>): Array<Vec2>;
    testArrayOfArray(a: Array<Int32Array>): Array<Int32Array>;
}

export interface /*record*/ VarnameRecord {
    Field: number;
}

export interface VarnameInterface {
    Rmethod(RArg: VarnameRecord): VarnameRecord;
    Imethod(IArg: VarnameInterface): VarnameInterface;
}

export interface /*record*/ ExtendedRecord {
    foo: boolean;
}

export interface /*record*/ RecordUsingExtendedRecord {
    er: ExtendedRecord;
}

export interface InterfaceUsingExtendedRecord {
    meth(er: ExtendedRecord): ExtendedRecord;
}

export interface ObjcOnlyListener {
}

export interface JavaOnlyListener {
}

export interface UsesSingleLanguageListeners {
    callForObjC(l: ObjcOnlyListener): void;
    returnForObjC(): ObjcOnlyListener;
    callForJava(l: JavaOnlyListener): void;
    returnForJava(): JavaOnlyListener;
}

export interface FirstListener {
    first(): void;
}

export interface SecondListener {
    second(): void;
}

export interface ListenerCaller {
    callFirst(): void;
    callSecond(): void;
}
export interface ListenerCaller_statics {
    init(firstL: FirstListener, secondL: SecondListener): ListenerCaller;
}

export interface ReturnOne {
    returnOne(): number;
}
export interface ReturnOne_statics {
    getInstance(): ReturnOne;
}

export interface ReturnTwo {
    returnTwo(): number;
}
export interface ReturnTwo_statics {
    getInstance(): ReturnTwo;
}

export interface /*record*/ ConstantRecord {
    someInteger: number;
    someString: string;
}

export interface /*record*/ Constants {
}

export interface ConstantsInterface {
    dummy(): void;
}

export interface /*record*/ AssortedPrimitives {
    b: boolean;
    eight: number;
    sixteen: number;
    thirtytwo: number;
    sixtyfour: bigint;
    fthirtytwo: number;
    fsixtyfour: number;
    oB: (boolean | null);
    oEight: (number | null);
    oSixteen: (number | null);
    oThirtytwo: (number | null);
    oSixtyfour: (bigint | null);
    oFthirtytwo: (number | null);
    oFsixtyfour: (number | null);
}

export interface TestHelpers {
}
export interface TestHelpers_statics {
    getSetRecord(): SetRecord;
    checkSetRecord(rec: SetRecord): boolean;
    getPrimitiveList(): PrimitiveList;
    checkPrimitiveList(pl: PrimitiveList): boolean;
    getNestedCollection(): NestedCollection;
    checkNestedCollection(nc: NestedCollection): boolean;
    getMap(): Map<string, bigint>;
    checkMap(m: Map<string, bigint>): boolean;
    getEmptyMap(): Map<string, bigint>;
    checkEmptyMap(m: Map<string, bigint>): boolean;
    getMapListRecord(): MapListRecord;
    checkMapListRecord(m: MapListRecord): boolean;
    checkClientInterfaceAscii(i: ClientInterface): void;
    checkClientInterfaceNonascii(i: ClientInterface): void;
    checkClientInterfaceArgs(i: ClientInterface): void;
    checkEnumMap(m: Map<Color, string>): void;
    checkEnum(c: Color): void;
    tokenId(t: UserToken): UserToken;
    createCppToken(): UserToken;
    checkCppToken(t: UserToken): void;
    cppTokenId(t: UserToken): bigint;
    checkTokenType(t: UserToken, type: string): void;
    returnNone(): (number | null);
    assortedPrimitivesId(i: AssortedPrimitives): AssortedPrimitives;
    idBinary(b: Uint8Array): Uint8Array;
}

export interface /*record*/ EmptyRecord {
}

export interface Conflict {
}

export interface ConflictUser {
    Conflict(): Conflict;
    conflictArg(cs: Set<Conflict>): boolean;
}

export interface SampleInterface {
}

export interface UserToken {
    whoami(): string;
}

export const enum Color {
    RED = 0,
    ORANGE = 1,
    YELLOW = 2,
    GREEN = 3,
    BLUE = 4,
    /**
     * "It is customary to list indigo as a color lying between blue and violet, but it has
     * never seemed to me that indigo is worth the dignity of being considered a separate
     * color. To my eyes it seems merely deep blue." --Isaac Asimov
     */
    INDIGO = 5,
    VIOLET = 6,
}

export interface /*record*/ EnumUsageRecord {
    e: Color;
    o: Color;
    l: Array<Color>;
    s: Set<Color>;
    m: Map<Color, Color>;
}

export interface EnumUsageInterface {
    e(e: Color): Color;
    o(o: Color): Color;
    l(l: Array<Color>): Array<Color>;
    s(s: Set<Color>): Set<Color>;
    m(m: Map<Color, Color>): Map<Color, Color>;
}

export interface /*record*/ ClientReturnedRecord {
    recordId: bigint;
    content: string;
    misc: string;
}

export interface ClientInterface {
    getRecord(recordId: bigint, utf8string: string, misc: string): ClientReturnedRecord;
    identifierCheck(data: Uint8Array, r: number, jret: bigint): number;
    returnStr(): string;
    methTakingInterface(i: ClientInterface): string;
    methTakingOptionalInterface(i: ClientInterface): string;
}

export interface ReverseClientInterface {
    returnStr(): string;
    methTakingInterface(i: ReverseClientInterface): string;
    methTakingOptionalInterface(i: ReverseClientInterface): string;
}
export interface ReverseClientInterface_statics {
    create(): ReverseClientInterface;
}

export interface CppException {
    throwAnException(): number;
}
export interface CppException_statics {
    get(): CppException;
}

export interface /*record*/ PrimitiveList {
    list: Array<bigint>;
}

export interface /*record*/ MapRecord {
    map: Map<string, bigint>;
    imap: Map<number, number>;
}

export interface /*record*/ MapListRecord {
    mapList: Array<Map<string, bigint>>;
}

export interface /*record*/ NestedCollection {
    setList: Array<Set<string>>;
}

export interface /*record*/ RecordWithDerivings {
    eight: number;
    sixteen: number;
    thirtytwo: number;
    sixtyfour: bigint;
    fthirtytwo: number;
    fsixtyfour: number;
    d: Date;
    s: string;
}

export interface /*record*/ RecordWithNestedDerivings {
    key: number;
    rec: RecordWithDerivings;
}

export interface /*record*/ SetRecord {
    set: Set<string>;
    iset: Set<number>;
}

export interface Module_statics {
    ProtoTests: ProtoTests_statics;
    TestOutcome: TestOutcome_statics;
    TestDuration: TestDuration_statics;
    DataRefTest: DataRefTest_statics;
    FlagRoundtrip: FlagRoundtrip_statics;
    TestArray: TestArray_statics;
    ListenerCaller: ListenerCaller_statics;
    ReturnOne: ReturnOne_statics;
    ReturnTwo: ReturnTwo_statics;
    TestHelpers: TestHelpers_statics;
    ReverseClientInterface: ReverseClientInterface_statics;
    CppException: CppException_statics;
}
