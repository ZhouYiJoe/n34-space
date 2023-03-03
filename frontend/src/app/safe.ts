import { Pipe } from "@angular/core";
import { DomSanitizer, SafeHtml } from "@angular/platform-browser";

@Pipe({ name: 'safeHtml' })
export class Safe {
  constructor(private sanitizer: DomSanitizer) { }
  // 转为安全的html
  public transform(html: string): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(html);
  }
}
