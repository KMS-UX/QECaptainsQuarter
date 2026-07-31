import React from 'react';
const TIER={main:'var(--quest-main)',side:'var(--quest-side)',faction:'var(--quest-faction)',world:'var(--quest-world)',daily:'var(--quest-daily)',hidden:'var(--quest-hidden)'};
const STATES={
  available:{g:'!',c:'var(--teal)',label:'Available'},
  accepted:{g:'○',c:'var(--electric-blue)',label:'Accepted'},
  'in-progress':{g:'◎',c:'var(--atom-gold)',label:'In Progress'},
  ready:{g:'!',c:'var(--success-green)',label:'Ready to Turn In'},
  completed:{g:'✔',c:'var(--success-green)',label:'Completed'},
  failed:{g:'✕',c:'var(--alert-red)',label:'Failed'},
  abandoned:{g:'✕',c:'var(--text-faint)',label:'Abandoned'},
  tracked:{g:'◆',c:'var(--teal)',label:'Tracked'},
  untracked:{g:'◇',c:'var(--text-faint)',label:'Untracked'},
};
export function QuestStatus({state='available',showLabel=true,size=16,style}){
  const s=STATES[state]||STATES.available;
  return <span style={{display:'inline-flex',alignItems:'center',gap:6,...style}}>
    <span style={{width:size,height:size,flex:'none',display:'inline-flex',alignItems:'center',justifyContent:'center',border:`1.5px solid ${s.c}`,borderRadius:'50%',color:s.c,fontSize:size*.6,lineHeight:1}}>{s.g}</span>
    {showLabel&&<span style={{fontSize:12,color:'var(--text-body)'}}>{s.label}</span>}
  </span>;
}
export function QuestTracker({title='Tracked Quests',quests=[],width=248,style}){
  return <div style={{width,background:'var(--surface-panel)',backgroundImage:'var(--scanline)',border:'1px solid var(--border-panel)',borderRadius:'var(--radius-1)',boxShadow:'var(--shadow-panel)',overflow:'hidden',...style}}>
    <div style={{padding:'7px 12px',borderBottom:'1px solid var(--border-panel)',fontFamily:'var(--font-display)',fontSize:10,fontWeight:700,letterSpacing:'var(--tracking-caps)',textTransform:'uppercase',color:'var(--text-section)'}}>{title}</div>
    <div style={{display:'flex',flexDirection:'column'}}>
      {quests.map((q,i)=>{const c=TIER[q.tier]||TIER.main; return <div key={i} style={{display:'flex',gap:9,padding:'9px 12px',borderLeft:`3px solid ${c}`,borderBottom:i<quests.length-1?'1px solid var(--border-panel)':'none',background:q.active?'var(--surface-raised)':'transparent'}}>
        <span style={{color:c,fontSize:12,lineHeight:1.3,flex:'none'}}>◆</span>
        <div style={{flex:1,minWidth:0}}>
          <div style={{fontFamily:'var(--font-display)',fontSize:12,fontWeight:600,color:q.active?'var(--fg-1)':'var(--text-body)',whiteSpace:'nowrap',overflow:'hidden',textOverflow:'ellipsis'}}>{q.title}</div>
          <div style={{fontSize:11,color:q.timer?'var(--atom-gold)':'var(--text-muted)',marginTop:2}}>{q.timer?('Ends in '+q.timer):q.sub}</div>
        </div>
      </div>;})}
    </div>
  </div>;
}
