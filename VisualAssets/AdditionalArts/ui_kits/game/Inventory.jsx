function Inventory({onBack}){
  const {Panel,ItemSlot,Tabs,RarityTag,Button,StatBar}=window.DS;
  const [tab,setTab]=React.useState('All');
  const [sel,setSel]=React.useState(0);
  const I=window.ICON;
  const items=[
    {r:'legendary',q:'+15',n:'Chrono Blade',t:'Weapon · Sword',img:I+'weapon-sword-legendary.png',d:'Deals heavy damage. May distort time on critical hits.'},
    {r:'epic',c:3,n:'Quantum Fragment',t:'Material',img:I+'crystal-essence.png'},{r:'rare',n:'Void Visor',t:'Armor · Head',img:I+'elem-void.png'},
    {r:'uncommon',c:12,n:'Solarite Shard',t:'Material',img:I+'gem-green.png'},{r:'common',c:5,n:'Ration Pack',t:'Consumable',img:I+'potion-health.png'},
    {r:'exotic',n:'Rift Anchor',t:'Relic',img:I+'gem-blue.png'},{r:'mythic',n:'Sin of Ambition',t:'Relic',img:I+'elem-quantum.png'},{r:'rare',n:'Pulse Rifle',t:'Weapon',img:I+'weapon-sword-rare.png'},
  ];
  const cur=items[sel];
  return <div style={{position:'absolute',inset:0,background:'var(--surface-page)',backgroundImage:'var(--scanline)',padding:28,display:'flex',flexDirection:'column',gap:16}}>
    <div style={{display:'flex',alignItems:'center',justifyContent:'space-between'}}>
      <h1 style={{fontSize:22,color:'var(--atom-gold)',margin:0}}>Inventory</h1>
      <div style={{display:'flex',gap:16,alignItems:'center'}}><window.Coin>12,450</window.Coin><window.Shard>318</window.Shard><Button variant="ghost" size="sm" onClick={onBack}>← Back</Button></div>
    </div>
    <Tabs tabs={["All","Weapon","Armor","Material","Relic","Consumable"]} active={tab} onChange={setTab}/>
    <div style={{display:'flex',gap:20,flex:1,minHeight:0}}>
      <div style={{display:'grid',gridTemplateColumns:'repeat(8,48px)',gap:8,alignContent:'flex-start'}}>
        {items.map((it,i)=><ItemSlot key={i} rarity={it.r} image={it.img} count={it.c} quality={it.q} selected={sel===i} onClick={()=>setSel(i)}/>)}
        {Array.from({length:24}).map((_,i)=><ItemSlot key={'e'+i} empty/>)}
      </div>
      <div style={{width:280,flex:'none'}}>
        <Panel title="Item Detail" accent={`var(--rarity-${cur.r})`}>
          <div style={{display:'flex',gap:12,alignItems:'flex-start'}}>
            <ItemSlot rarity={cur.r} image={cur.img} size={64} quality={cur.q}/>
            <div style={{display:'flex',flexDirection:'column',gap:6,flex:1,minWidth:0}}>
              <div style={{fontFamily:'var(--font-display)',fontSize:13,lineHeight:1.25,color:'var(--fg-1)'}}>{cur.n}</div>
              <div><RarityTag rarity={cur.r}/></div>
              <div style={{fontSize:11,color:'var(--text-muted)'}}>{cur.t}</div>
            </div>
          </div>
          {cur.d&&<p style={{fontSize:12,color:'var(--text-muted)',lineHeight:1.5}}>{cur.d}</p>}
          <div style={{fontFamily:'var(--font-display)',fontSize:10,letterSpacing:'.12em',color:'var(--text-faint)',margin:'8px 0 4px'}}>STATS</div>
          <div style={{display:'flex',flexDirection:'column',gap:5}}>
            <StatBar kind="hp" label="ATK" value={cur.r==='legendary'?92:40} max={100} showValue={false} height={8}/>
            <StatBar kind="ep" label="MAG" value={cur.r==='legendary'?70:30} max={100} showValue={false} height={8}/>
          </div>
          <div style={{display:'flex',gap:8,marginTop:14}}><Button size="sm">Equip</Button><Button variant="ghost" size="sm">Drop</Button></div>
        </Panel>
      </div>
    </div>
  </div>;
}
window.Inventory=Inventory;
