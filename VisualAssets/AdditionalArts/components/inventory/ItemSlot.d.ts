/** Inventory slot with rarity frame (7-tier color + glow for epic+), stack count badge, enhancement badge (+5…MAX, green), state badge (new/favorite/equipped/locked), selected state. RarityTag: caps label chip in tier color. */
export interface ItemSlotProps {
  rarity?: 'common'|'uncommon'|'rare'|'epic'|'legendary'|'mythic'|'exotic';
  icon?: string;
  image?: string;
  count?: number;
  quality?: string;
  badge?: 'new'|'favorite'|'equipped'|'locked';
  selected?: boolean;
  size?: number;
  empty?: boolean;
  onClick?: () => void;
  style?: React.CSSProperties;
}
export declare function ItemSlot(props: ItemSlotProps): JSX.Element;
export interface RarityTagProps { rarity?: ItemSlotProps['rarity']; children?: React.ReactNode; style?: React.CSSProperties; }
export declare function RarityTag(props: RarityTagProps): JSX.Element;
