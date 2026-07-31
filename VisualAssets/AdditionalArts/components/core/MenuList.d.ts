/** Vertical main-menu button stack (CONTINUE / NEW GAME / LOAD GAME…), selected item glows teal. */
export interface MenuListProps {
  items?: string[];
  selected?: string;
  onSelect?: (item: string) => void;
  width?: number | string;
}
export declare function MenuList(props: MenuListProps): JSX.Element;
