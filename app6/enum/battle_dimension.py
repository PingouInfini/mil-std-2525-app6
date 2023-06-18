from enum import Enum, unique


@unique
class BattleDimension(str, Enum):
    P = "SPACE"
    A = "AIR"
    G = "GROUND"
    GU = "GROUND UNIT"
    GE = "GROUND EQUIPMENT"
    GI = "GROUND INSTALLATION"
    S = "SEA SURFACE"
    U = "SUBSURFACE"
