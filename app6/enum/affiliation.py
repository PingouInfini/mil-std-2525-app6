from enum import Enum, unique

from app6.svg_shape.assumed_friend_shapes import assumed_friend_shapes
from app6.svg_shape.friend_shapes import friend_shapes
from app6.svg_shape.hostile_shapes import hostile_shapes
from app6.svg_shape.neutral_shapes import neutral_shapes
from app6.svg_shape.pending_shapes import pending_shapes
from app6.svg_shape.suspect_shapes import suspect_shapes
from app6.svg_shape.unknown_shapes import unknown_shapes


@unique
class Affiliation(str, Enum):
    F = "FRIEND"
    A = "ASSUMED FRIEND"
    H = "HOSTILE"
    S = "SUSPECT"
    N = "NEUTRAL"
    U = "UNKNOWN"
    P = "PENDING"

    def get_shape(self, battle_dimension):

        if self == Affiliation.F and battle_dimension in friend_shapes:
            return friend_shapes[battle_dimension]

        elif self == Affiliation.A and battle_dimension in assumed_friend_shapes:
            return assumed_friend_shapes[battle_dimension]

        elif self == Affiliation.H and battle_dimension in hostile_shapes:
            return hostile_shapes[battle_dimension]

        elif self == Affiliation.S and battle_dimension in suspect_shapes:
            return suspect_shapes[battle_dimension]

        elif self == Affiliation.N and battle_dimension in neutral_shapes:
            return neutral_shapes[battle_dimension]

        elif self == Affiliation.U and battle_dimension in unknown_shapes:
            return unknown_shapes[battle_dimension]

        elif self == Affiliation.P and battle_dimension in pending_shapes:
            return pending_shapes[battle_dimension]

        return ""
