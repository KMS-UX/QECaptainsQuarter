/** In-game objective checklist (§07): diamond bullets, green check + strikethrough when done, count column (e.g. 2/5). */
export interface Objective { text: string; done?: boolean; count?: string; }
export interface ObjectiveListProps { title?: string; items?: Objective[]; style?: React.CSSProperties; }
export declare function ObjectiveList(props: ObjectiveListProps): JSX.Element;
