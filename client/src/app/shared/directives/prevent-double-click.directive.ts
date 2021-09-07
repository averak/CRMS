import { Directive, ElementRef, HostListener, Input } from '@angular/core';

@Directive({
  selector: 'button[appPreventDoubleClick]',
})
export class PreventDoubleClickDirective {
  @Input() disableTime = 300;

  constructor(private elementRef: ElementRef) {}

  @HostListener('click', ['$event.target'])
  onClick() {
    this.elementRef.nativeElement.disabled = true;
    setTimeout(() => (this.elementRef.nativeElement.disabled = false), this.disableTime);
  }
}
