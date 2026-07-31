/** Inventory filter tabs (ALL / EQUIP / ITEM / QUEST / MISC) — gold underline on the active tab. */
export interface TabsProps {
  tabs?: string[];
  active?: string;
  onChange?: (tab: string) => void;
}
export declare function Tabs(props: TabsProps): JSX.Element;
